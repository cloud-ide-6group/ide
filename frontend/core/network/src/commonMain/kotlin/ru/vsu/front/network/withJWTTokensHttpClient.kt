package ru.vsu.front.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.RefreshUseCase
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

/**
 * Создает и настраивает экземпляр [HttpClient] для выполнения запросов, в заголовке которых может быть токен.
 * * Использует движок [CIO].
 *
 * * @property baseUrl Базовый url для запросов.
 * * @property tokenStorage Хранилище токенов.
 * * @property refreshUseCase Юзкейс для обновления токенов.
 * * @property authManager Менеджер аутентификации.
 *
 * @return Готовый HTTP-клиент.
 */
fun withJWTTokensHttpClient(
    baseUrl: String,
    tokenStorage: TokenStorage,
    refreshUseCase: RefreshUseCase,
    authManager: AuthManager
): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url(baseUrl)
        }

        /**
         * Авто использование токенов, обновление.
         */
        install(Auth) {
            /**
             * Bearer.
             */
            bearer {
                /**
                 * Загрузка токенов при создании клиента.
                 */
                loadTokens {
                    val tokens = tokenStorage.getTokens()

                    return@loadTokens tokens?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }

                /**
                 * Обновление токенов при получении ошибки 401.
                 */
                refreshTokens {
                    val oldTokens = tokenStorage.getTokens() ?: return@refreshTokens null

                    when (val response = refreshUseCase(oldTokens.refreshToken)) {
                        is Response.Error<*> -> {
                            tokenStorage.clearTokens()
                            authManager.logout()
                            null
                        }

                        is Response.Success<AuthTokens> -> {
                            val newAccess = response.data.accessToken
                            val newRefresh = response.data.refreshToken

                            tokenStorage.saveToken(newAccess, isAccess = true)
                            tokenStorage.saveToken(newRefresh, isAccess = false)

                            BearerTokens(newAccess, newRefresh)
                        }
                    }
                }
            }
        }
    }
}