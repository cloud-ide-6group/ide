package ru.vsu.front.features.auth.data.repository

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.features.auth.data.HttpRoutes.LOGIN
import ru.vsu.front.features.auth.data.HttpRoutes.SIGN
import ru.vsu.front.features.auth.data.dto.AuthResponseDto
import ru.vsu.front.features.auth.data.dto.ErrorResponseDto
import ru.vsu.front.features.auth.data.dto.UserLoginRequest
import ru.vsu.front.features.auth.data.dto.UserSignRequest
import ru.vsu.front.features.auth.data.mapper.toEntity
import ru.vsu.front.features.auth.domain.entity.AuthError
import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.repository.AuthRepository

/**
 * Реализация интерфейса [AuthRepository] для работы с сетевым API.
 *
 * @param httpClient Клиент Ktor для выполнения запросов.
 */
class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {

    /**
     * Выполняет POST-запрос на эндпоинт авторизации ([LOGIN]).
     *
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return [AuthResult.Success] при успешном входе (200).
     * @return [AuthError.Forbidden] при неверных учетных данных (403).
     * @return [AuthError.NetworkException] при ошибке сети.
     */
    override suspend fun login(
        email: String,
        password: String
    ): AuthResult<UserSession> {
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
                    val dto = response.body<AuthResponseDto>()
                    AuthResult.Success(dto.toEntity())
                }

                HttpStatusCode.Forbidden -> {
                    val message = response.body<ErrorResponseDto>().message
                    AuthResult.Error(AuthError.Forbidden(message))
                }

                else -> {
                    AuthResult.Error(AuthError.UnknownError())
                }
            }
        } catch (_: Exception) {
            AuthResult.Error(AuthError.NetworkException())
        }
    }

    /**
     * Выполняет POST-запрос на эндпоинт регистрации ([SIGN]).
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return [AuthResult.Success] при успешной регистрации (201).
     * @return [AuthError.BadRequest] при ошибке валидации данных на сервере (400).
     * @return [AuthError.NetworkException] при ошибке сети.
     */
    override suspend fun sign(
        name: String,
        email: String,
        password: String
    ): AuthResult<UserSession> {
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
                    val dto = response.body<AuthResponseDto>()
                    AuthResult.Success(dto.toEntity())
                }

                HttpStatusCode.BadRequest -> {
                    val message = response.body<ErrorResponseDto>().message
                    AuthResult.Error(AuthError.BadRequest(message))
                }

                else -> {
                    AuthResult.Error(AuthError.UnknownError())
                }
            }
        } catch (_: Exception) {
            AuthResult.Error(AuthError.NetworkException())
        }
    }
}