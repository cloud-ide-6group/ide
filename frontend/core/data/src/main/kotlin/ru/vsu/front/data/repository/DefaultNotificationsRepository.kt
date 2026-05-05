package ru.vsu.front.data.repository

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Notification

/**
 * Реализация интерфейса [NotificationsRepository] для работы с сетевым API.
 *
 * @param baseUrl Базовый url для запросов.
 * @param tokenStorage Хранилище токенов.
 */
class DefaultNotificationsRepository(
    private val tokenStorage: TokenStorage,
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
            extraHeaders = mapOf("Authorization" to listOf("Bearer ${tokens.accessToken}"))
            transports = arrayOf(Polling.NAME)
        }

        val socket = IO.socket(baseUrl, options)

        socket.on("notifications_list") { args ->
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
            socket.disconnect()
            socket.off()
        }
    }
}