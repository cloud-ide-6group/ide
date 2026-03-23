package ru.vsu.front.data.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.dto.UserDataDto
import ru.vsu.front.data.dto.AuthTokensDto
import ru.vsu.front.data.dto.ErrorResponseDto
import ru.vsu.front.data.dto.UpdateTokensRequest
import ru.vsu.front.data.dto.UserLoginRequest
import ru.vsu.front.data.dto.UserSignRequest
import ru.vsu.front.data.mapper.toEntity
import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.UserData
import ru.vsu.front.network.HttpRoutes.LOGIN
import ru.vsu.front.network.HttpRoutes.REFRESH_TOKENS
import ru.vsu.front.network.HttpRoutes.SIGN

/**
 * Реализация интерфейса [AuthRepository] для работы с сетевым API.
 *
 * @param httpClient Клиент Ktor для выполнения запросов.
 */
class DefaultAuthRepository(
    private val httpClient: HttpClient
) : AuthRepository {

    /**
     * Выполняет POST-запрос на эндпоинт авторизации ([LOGIN]).
     *
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return [Response.Success] при успешном входе (200).
     * @return [RequestError.Forbidden] при неверных учетных данных (403).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun login(
        email: String,
        password: String
    ): Response<UserData> {
        return try {
            val response = httpClient.post(LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(
                    UserLoginRequest(
                        email = email,
                        password = password
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val dto = response.body<UserDataDto>()
                    Response.Success(dto.toEntity())
                }

                HttpStatusCode.Forbidden -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Forbidden(message))
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
     * Выполняет POST-запрос на эндпоинт регистрации ([SIGN]).
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return [Response.Success] при успешной регистрации (201).
     * @return [RequestError.BadRequest] при ошибке валидации данных на сервере (400).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun sign(
        name: String,
        email: String,
        password: String
    ): Response<UserData> {
        return try {
            val response = httpClient.post(SIGN) {
                contentType(ContentType.Application.Json)
                setBody(
                    UserSignRequest(
                        name = name,
                        email = email,
                        password = password
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    val dto = response.body<UserDataDto>()
                    Response.Success(dto.toEntity())
                }

                HttpStatusCode.BadRequest -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.BadRequest(message))
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
     * Выполняет POST-запрос на эндпоинт обновления токенов ([REFRESH_TOKENS]).
     *
     * @param accessToken Токен доступа.
     * @param refreshToken Токен обновления.
     * @return [Response.Success] при успешном обновлении.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun refresh(
        accessToken: String,
        refreshToken: String
    ): Response<AuthTokens> {
        return try {
            val response = httpClient.post(REFRESH_TOKENS) {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateTokensRequest(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val tokens = response.body<AuthTokensDto>()
                    Response.Success(tokens.toEntity())
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
            Response.Error(RequestError.NetworkException())
        }
    }
}