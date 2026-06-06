package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.socket.client.IO
import io.socket.engineio.client.transports.Polling
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.datastore.token_storage.TokenStorage
import ru.vsu.front.domain.repository.PremiumRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.SUBSCRIBE
import ru.vsu.front.network.MainHttpClientManager
import ru.vsu.front.network.SocketRoutes.SUBSCRIPTION_EXPIRED

/**
 * Реализация интерфейса [PremiumRepository] для работы с платными функциями.
 *
 * @property mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 * @property tokenStorage Хранилище токенов.
 * @property baseUrl Базовый url для запросов.
 */
class DefaultPremiumRepository(
    private val mainHttpClientManager: MainHttpClientManager,
    private val tokenStorage: TokenStorage,
    private val baseUrl: String
) : PremiumRepository {

    /**
     * Выполняет запрос на эндпоинт оформления подписки ([SUBSCRIBE]).
     *
     * @return [Response.Success] с не важно чем, важен лишь код ответа либо с ошибкой.
     */
    override suspend fun subscribe(): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().get(SUBSCRIBE) {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> Response.Error(RequestError.UnknownError())
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    /**
     * Отправляет запрос на получение контента файла.
     *
     * @return [Flow], отправляющий текст файла.
     */
    override fun observeSubscriptionExpired(): Flow<Boolean> = callbackFlow {
        val tokens = tokenStorage.getTokensSync()

        if (tokens == null) {
            close(Exception("Token is null"))
            return@callbackFlow
        }

        val options = IO.Options().apply {
            auth = mapOf(
                "token" to tokens.accessToken
            )
            transports = arrayOf(Polling.NAME)
            reconnection = true
            reconnectionAttempts = 10
            reconnectionDelay = 1000
            reconnectionDelayMax = 5000
        }

        val socket = IO.socket(baseUrl, options)

        socket.on(SUBSCRIPTION_EXPIRED) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject
                if (data == null) {
                    return@on
                }

                val isExpired = data.optBoolean("is_expired", true)
                trySend(isExpired)
            } catch (_: Exception) {
            }
        }

        socket.connect()

        awaitClose {
            socket.disconnect()
            socket.off(SUBSCRIPTION_EXPIRED)
            socket.close()
        }
    }
}