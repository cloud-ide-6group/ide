package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.entity.dto.ProgramingLanguageDto
import ru.vsu.front.data.mapper.toEntities
import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.domain.repository.ProgramingLanguageRepository
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.PROFILE
import ru.vsu.front.network.HttpRoutes.PROGRAMING_LANGUAGES
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [ProgramingLanguageRepository] для работы с сетевым API.
 *
 * @param mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 */
class DefaultProgramingLanguageRepository(
    private val mainHttpClientManager: MainHttpClientManager
) : ProgramingLanguageRepository {
    /**
     * Выполняет GET-запрос на эндпоинт получения языков программирования ([PROGRAMING_LANGUAGES]).
     *
     * @return [Response.Success] с доступными языками программирования при успешном запросе.
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun getProgramingLanguages(): Response<List<ProgramingLanguage>> {
        return try {
            val response = mainHttpClientManager.getClient().get(PROGRAMING_LANGUAGES) {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val languages = response.body<List<ProgramingLanguageDto>>()
                    Response.Success(languages.toEntities())
                }

                else -> Response.Error(RequestError.UnknownError())
            }
        } catch (_: Exception) {
            Response.Error(RequestError.NetworkException())
        }
    }
}