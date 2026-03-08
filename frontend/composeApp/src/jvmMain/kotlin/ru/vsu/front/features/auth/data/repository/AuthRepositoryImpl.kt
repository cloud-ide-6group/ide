@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
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
 * Репозиториий ответственный за авторизацию и регистрацию
 *
 * @param httpClient HttpClient, ktor-client для работы с сетью
 */
class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {

    /**
     * Функция авторизации
     *
     * @param email Почта пользователя
     * @param password Пароль пользователя
     */
    override suspend fun login(
        email: String,
        password: String
    ): AuthResult<UserSession> {
        return try {
            val response = httpClient.post(LOGIN) {
                contentType(ContentType.Application.Json)
                setBody(UserLoginRequest(
                    email = email,
                    password = password
                ))
            }

            when(response.status) {
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
     * Функция регистрации
     *
     * @param name Имя пользователя
     * @param email Почта пользователя
     * @param password Пароль пользователя
     */
    override suspend fun sign(
        name: String,
        email: String,
        password: String
    ): AuthResult<UserSession> {
        return try {
            val response = httpClient.post(SIGN) {
                contentType(ContentType.Application.Json)
                setBody(UserSignRequest(
                    name = name,
                    email = email,
                    password = password
                ))
            }

            when(response.status) {
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