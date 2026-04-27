package ru.vsu.front.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Создает и настраивает экземпляр [HttpClient] для выполнения запросов, в заголовке которых не передаётся токен.
 * * Использует движок [CIO].
 * * @param baseUrl базовый url для запросов.
 *
 * @return Готовый HTTP-клиент.
 */
fun withoutJWTTokensHttpClient(baseUrl: String): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url(baseUrl)
        }
    }
}