package ru.vsu.front.data.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.dto.ProgramingLanguageDto
import ru.vsu.front.data.mapper.toEntities
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.ProgramingLanguageRepository
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.PROGRAMING_LANGUAGES
import ru.vsu.front.network.MainHttpClientManager

class DefaultProgramingLanguageRepository(
    private val mainHttpClientManager: MainHttpClientManager
) : ProgramingLanguageRepository {
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