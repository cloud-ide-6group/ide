package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.data.entity.dto.ProgramingLanguageDto
import ru.vsu.front.data.mapper.toEntities
import ru.vsu.front.domain.repository.PremiumRepository
import ru.vsu.front.domain.repository.ProgramingLanguageRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.PROGRAMING_LANGUAGES
import ru.vsu.front.network.HttpRoutes.SUBSCRIBE
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [PremiumRepository] для работы с платными функциями.
 *
 * @property mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 */
class DefaultPremiumRepository(
    private val mainHttpClientManager: MainHttpClientManager
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
                    val languages = response.body<List<ProgramingLanguageDto>>()
                    Response.Success(languages.toEntities())
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
}