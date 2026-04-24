package ru.vsu.front.data.repository

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import ru.vsu.front.data.dto.CreateProjectRequest
import ru.vsu.front.data.dto.CreateProjectResponse
import ru.vsu.front.data.dto.ErrorResponseDto
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [ProjectRepository] для работы с сетевым API.
 *
 * @param mainHttpClient Клиент Ktor для выполнения запросов.
 */
class DefaultProjectRepository(
    private val mainHttpClientManager: MainHttpClientManager
) : ProjectRepository {
    override suspend fun createProject(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int> {
        return try {
            val response = mainHttpClientManager.getClient().post(HttpRoutes.CREATE_PROJECT) {
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