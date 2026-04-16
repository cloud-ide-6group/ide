package ru.vsu.front.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.RefreshUseCase
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
    refreshUseCase: RefreshUseCase
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
                    val tokens = tokenStorage.getTokens()
                    if (tokens != null) {
                        BearerTokens(tokens.accessToken, tokens.refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val oldTokens = tokenStorage.getTokens() ?: return@refreshTokens null

                    val response = refreshUseCase(oldTokens.accessToken, oldTokens.refreshToken)

                    if (response is Response.Success) {
                        val newAccess = response.data.accessToken
                        val newRefresh = response.data.refreshToken

                        tokenStorage.saveToken(newAccess, isAccess = true)
                        tokenStorage.saveToken(newRefresh, isAccess = false)

                        BearerTokens(newAccess, newRefresh)
                    } else {
                        null
                    }
                }
            }
        }
    }
}