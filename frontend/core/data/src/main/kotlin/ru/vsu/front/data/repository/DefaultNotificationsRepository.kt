package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.data.entity.request.DeleteNotificationRequest
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.DELETE_NOTIFICATION
import ru.vsu.front.network.MainHttpClientManager
import ru.vsu.front.network.SocketRoutes.FILES_TREES_LIST
import ru.vsu.front.network.SocketRoutes.NOTIFICATIONS_LIST

/**
 * Реализация интерфейса [NotificationsRepository] для работы с сетевым API.
 *
 * @property tokenStorage Хранилище токенов.
 * @property mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 * @property baseUrl Базовый url для запросов.
 */
class DefaultNotificationsRepository(
    private val tokenStorage: TokenStorage,
    private val mainHttpClientManager: MainHttpClientManager,
    private val baseUrl: String
) : NotificationsRepository {
    /**
     * Выполняет подписку на получение уведомлений, используя AccessToken из хранилища.
     */
    override fun observeNotifications(): Flow<List<Notification>> = callbackFlow {
        val tokens = tokenStorage.getTokens()

        if (tokens == null) {
            close(Exception("Token is null"))
            return@callbackFlow
        }

        val options = IO.Options().apply {
            auth = mapOf(
                "token" to tokens.accessToken
            )
            transports = arrayOf(Polling.NAME)
        }

        val socket = IO.socket(baseUrl, options)

        socket.on(NOTIFICATIONS_LIST) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject
                if (data == null) return@on

                val notificationsArray = data.optJSONArray("notifications") ?: return@on

                val notificationsList = mutableListOf<Notification>()

                for (i in 0 until notificationsArray.length()) {
                    val item = notificationsArray.getJSONObject(i)

                    val notificationId = item.optInt("notification_id", 0)
                    val senderName = item.optString("sender_name", null)
                    val sendTime = item.optString("send_time", null)
                    val projectId = item.optInt("project_id", 0)
                    val projectName = item.optString("project_name", null)

                    if (notificationId == 0 || senderName == null || projectName == null || projectId == 0 || sendTime == null) {
                        continue
                    }

                    notificationsList.add(
                        Notification(
                            notificationId = notificationId,
                            senderName = senderName,
                            sendTime = sendTime,
                            projectId = projectId,
                            projectName = projectName
                        )
                    )
                }

                trySend(notificationsList)
            } catch (_: Exception) {
            }
        }

        socket.on(Socket.EVENT_CONNECT_ERROR) {
        }

        socket.connect()

        awaitClose {
            socket.off(FILES_TREES_LIST)
        }
    }

    /**
     * Отправляет HTTP DELETE-запрос для удаления конкретного уведомления у пользователя.
     *
     * @param notificationId Уникальный идентификатор уведомления, которое необходимо удалить.
     *
     * @return [Response] с результатом выполнения запроса.
     */
    override suspend fun deleteNotification(notificationId: Int): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().delete(DELETE_NOTIFICATION) {
                contentType(ContentType.Application.Json)
                setBody(
                    DeleteNotificationRequest(
                        notificationId = notificationId
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.NotFound(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }

        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }
}