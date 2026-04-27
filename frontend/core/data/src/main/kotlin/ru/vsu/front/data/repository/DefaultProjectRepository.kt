package ru.vsu.front.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.entity.request.CreateProjectRequest
import ru.vsu.front.data.entity.response.CreateProjectResponse
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes
import ru.vsu.front.network.HttpRoutes.CREATE_PROJECT
import ru.vsu.front.network.HttpRoutes.PROGRAMING_LANGUAGES
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [ProjectRepository] для работы с сетевым API.
 *
 * @param mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 */
class DefaultProjectRepository(
    private val mainHttpClientManager: MainHttpClientManager
) : ProjectRepository {
    /**
     * Выполняет POST-запрос на эндпоинт создания проекта ([CREATE_PROJECT]).
     *
     * @return [Response.Success] с доступными языками программирования при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.Forbidden] при неверных учетных данных (403).
     * @return [RequestError.Conflict] при ошибке создания проекта (409).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun createProject(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int> {
        return try {
            val response = mainHttpClientManager.getClient().post(CREATE_PROJECT) {
                contentType(ContentType.Application.Json)
                setBody(CreateProjectRequest(
                    programingLanguageId = programingLanguageId,
                    projectName = projectName
                ))
            }

            when(response.status) {
                HttpStatusCode.Created -> {
                    val createdProjectId = response.body<CreateProjectResponse>().projectId
                    Response.Success(createdProjectId)
                }

                HttpStatusCode.Unauthorized -> {
                    Response.Error(RequestError.Unauthorized(""))
                }

                HttpStatusCode.Forbidden -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Forbidden(message))
                }

                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error(RequestError.NetworkException())
        }
    }
}