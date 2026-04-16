package ru.vsu.front.data.repository

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.dto.ErrorResponseDto
import ru.vsu.front.data.dto.UserDto
import ru.vsu.front.data.mapper.toEntity
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User
import ru.vsu.front.network.HttpRoutes.PROFILE

/**
 * Реализация интерфейса [ProfileRepository] для работы с сетевым API.
 *
 * @param mainHttpClient Клиент Ktor для выполнения запросов.
 * @param tokenStorage Хранилище токенов.
 */
class DefaultProfileRepository(
    private val mainHttpClient: HttpClient,
    private val tokenStorage: TokenStorage
) : ProfileRepository {

    override suspend fun getProfile(userId: Int): Response<User> {
        return try {
            val response = mainHttpClient.get(PROFILE) {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val dto = response.body<UserDto>()

                    val id = tokenStorage.getUserIdFromToken()
                        ?: return Response.Error(RequestError.UnknownError("Invalid token payload"))

                    Response.Success(dto.toEntity(id))
                }

                HttpStatusCode.Unauthorized -> {
                    Response.Error(RequestError.Unauthorized(""))
                }

                HttpStatusCode.Forbidden -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Forbidden(message))
                }

                HttpStatusCode.NotFound -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.NotFound(message))
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