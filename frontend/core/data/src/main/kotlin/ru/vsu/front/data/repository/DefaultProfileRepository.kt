package ru.vsu.front.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.data.entity.request.UpdateProfileDataRequest
import ru.vsu.front.data.entity.request.UpdateProfilePasswordRequest
import ru.vsu.front.data.entity.request.UpdateProfilePhotoRequest
import ru.vsu.front.data.entity.dto.UserProfileDto
import ru.vsu.front.data.mapper.toEntity
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserProfile
import ru.vsu.front.network.HttpRoutes.PROFILE
import ru.vsu.front.network.HttpRoutes.REFRESH_TOKENS
import ru.vsu.front.network.HttpRoutes.UPDATE_PROFILE_DATA
import ru.vsu.front.network.HttpRoutes.UPDATE_PROFILE_PASSWORD
import ru.vsu.front.network.HttpRoutes.UPDATE_PROFILE_PHOTO
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [ProfileRepository] для работы с сетевым API.
 *
 * @param mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 * @param tokenStorage Хранилище токенов.
 */
class DefaultProfileRepository(
    private val mainHttpClientManager: MainHttpClientManager,
    private val tokenStorage: TokenStorage
) : ProfileRepository {

    /**
     * Выполняет GET-запрос на эндпоинт получения профиля ([PROFILE]).
     *
     * @return [Response.Success] с профилем пользователя при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.NotFound] при неверных данных или пользователь не найден (404).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
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

                HttpStatusCode.Unauthorized -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Unauthorized(message))
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

    /**
     * Выполняет PUT-запрос на эндпоинт обновление почты и имени пользователя ([UPDATE_PROFILE_DATA]).
     *
     * @return [Response.Success] при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.NotFound] при неверных данных или пользователь не найден (404).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
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

                HttpStatusCode.Unauthorized -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Unauthorized(message))
                }

                HttpStatusCode.NotFound -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.NotFound(message))
                }

                else -> {
                    println(response.bodyAsText())
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<Any>(RequestError.NetworkException())
        }
    }

    /**
     * Выполняет PUT-запрос на эндпоинт обновление пароля пользователя ([UPDATE_PROFILE_PASSWORD]).
     *
     * @return [Response.Success] при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.Conflict] если пользователь не найден (409).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
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

                HttpStatusCode.Unauthorized -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Unauthorized(message))
                }

                HttpStatusCode.Conflict -> {
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

    /**
     * Выполняет PUT-запрос на эндпоинт обновление аватара пользователя ([UPDATE_PROFILE_PHOTO]).
     *
     * @return [Response.Success] при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.Conflict] если пользователь не найден (409).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
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
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<Any>(RequestError.NetworkException())
        }
    }
}