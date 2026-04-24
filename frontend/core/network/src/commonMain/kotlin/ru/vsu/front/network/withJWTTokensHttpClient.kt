package ru.vsu.front.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.auth.AuthState
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.RefreshUseCase
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

/**
 * Создает и настраивает экземпляр [HttpClient] для выполнения запросов, в заголовке которых может быть токен.
 * * Использует движок [CIO].
 * * @param baseUrl базовый url для запросов.
 *
 * @return Готовый HTTP-клиент.
 */
fun mainHttpClient(
    baseUrl: String,
    tokenStorage: TokenStorage,
    refreshUseCase: RefreshUseCase,
    authManager: AuthManager
): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url(baseUrl)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    if (authManager.isAuthorized.value is AuthState.NotAuthorized) return@loadTokens null

                    val tokens = tokenStorage.getTokens()

                    if (tokens != null) {
                        BearerTokens(tokens.accessToken, tokens.refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val oldTokens = tokenStorage.getTokens() ?: return@refreshTokens null

                    when(val response = refreshUseCase(oldTokens.accessToken, oldTokens.refreshToken)) {
                        is Response.Error<*> -> {
                            tokenStorage.clearTokens()
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