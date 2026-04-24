package ru.vsu.front.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.dto.ErrorResponseDto
import ru.vsu.front.data.dto.UpdateProfileDataRequest
import ru.vsu.front.data.dto.UpdateProfilePasswordRequest
import ru.vsu.front.data.dto.UpdateProfilePhotoRequest
import ru.vsu.front.data.dto.UserProfileDto
import ru.vsu.front.data.mapper.toEntity
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserProfile
import ru.vsu.front.network.HttpRoutes.PROFILE
import ru.vsu.front.network.HttpRoutes.UPDATE_PROFILE_DATA
import ru.vsu.front.network.HttpRoutes.UPDATE_PROFILE_PASSWORD
import ru.vsu.front.network.HttpRoutes.UPDATE_PROFILE_PHOTO
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [ProfileRepository] для работы с сетевым API.
 *
 * @param TODO Клиент Ktor для выполнения запросов.
 * @param tokenStorage Хранилище токенов.
 */
class DefaultProfileRepository(
    private val mainHttpClientManager: MainHttpClientManager,
    private val tokenStorage: TokenStorage
) : ProfileRepository {

    override suspend fun getProfile(): Response<UserProfile> {
        return try {
            val response = mainHttpClientManager.getClient().get(PROFILE) {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val dto = response.body<UserProfileDto>()

                    val id = tokenStorage.getUserIdFromToken()
                        ?: return Response.Error(RequestError.UnknownError("Invalid token payload"))

                    Response.Success(dto.toEntity(id))
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

    override suspend fun updateProfileData(
        email: String,
        name: String,
    ): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().put(UPDATE_PROFILE_DATA) {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProfileDataRequest(
                        email = email,
                        name = name,
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Any())
                }

                HttpStatusCode.NotFound -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.NotFound(message))
                }

                HttpStatusCode.Unauthorized -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Unauthorized(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<Any>(RequestError.NetworkException())
        }
    }

    override suspend fun updateProfilePassword(
        newPassword: String,
        oldPassword: String
    ): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().put(UPDATE_PROFILE_PASSWORD) {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProfilePasswordRequest(
                        newPassword = newPassword,
                        oldPassword = oldPassword
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Any())
                }

                HttpStatusCode.NotFound -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.NotFound(message))
                }

                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Unauthorized(message))
                }

                else -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.UnknownError(message))
                }
            }
        } catch (e: Exception) {
            Response.Error<Any>(RequestError.NetworkException(e.message ?: "unknown error"))
        }
    }

    override suspend fun updateProfilePhoto(photoBase64: String): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().put(UPDATE_PROFILE_PHOTO) {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProfilePhotoRequest(
                        photoBase64 = photoBase64
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Any())
                }

                HttpStatusCode.Unauthorized -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.NotFound(message))
                }

                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Unauthorized(message))
                }

                else -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.UnknownError(message))
                }
            }
        } catch (_: Exception) {
            Response.Error<Any>(RequestError.NetworkException())
        }
    }
}