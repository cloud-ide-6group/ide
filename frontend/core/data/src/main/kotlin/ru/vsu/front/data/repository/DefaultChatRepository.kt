package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.data.entity.request.CreateChatRequest
import ru.vsu.front.data.entity.request.CreateMessageRequest
import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.domain.repository.ChatRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.CREATE_CHAT
import ru.vsu.front.network.HttpRoutes.CREATE_MESSAGE
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [AuthRepository].
 *
 * @property mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 */
class DefaultChatRepository(
    private val mainHttpClientManager: MainHttpClientManager
) : ChatRepository {

    /**
     * Выполняет POST запрос на создание нового чата.
     *
     * @param projectId Идентификатор проекта.
     * @param identificator Строковый идентификатор чата.
     *
     * @return [Response.Success] если чат успешно создан.
     * @return [Response.Error] при ошибке.
     */
    override suspend fun createChat(projectId: Int, identificator: String): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().post(CREATE_CHAT) {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateChatRequest(
                        projectId = projectId,
                        identificator = identificator
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    /**
     * Выполняет POST запрос на создание сообщения.
     *
     * @param chatId Идентификатор чата.
     * @param text Текст сообщения.
     *
     * @return [Response.Success] если сообщения успешно создано.
     * @return [Response.Error] при ошибке.
     */
    override suspend fun createMessage(chatId: Int, text: String): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().post(CREATE_MESSAGE) {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateMessageRequest(
                        chatId = chatId,
                        text = text
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
                    Response.Error(RequestError.Conflict(message))
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