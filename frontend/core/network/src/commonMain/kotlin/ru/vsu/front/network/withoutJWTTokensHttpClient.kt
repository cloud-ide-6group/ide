package ru.vsu.front.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
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