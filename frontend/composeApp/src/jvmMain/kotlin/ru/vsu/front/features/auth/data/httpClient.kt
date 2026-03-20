package ru.vsu.front.features.auth.data

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.vsu.front.config.BuildKonfig

/**
 * Создает и настраивает экземпляр [HttpClient].
 * * Использует движок [CIO].
 * * Автоматически сериализует/десериализует JSON.
 * * Подставляет базовый URL из конфигурации сборки ([BuildKonfig.BASE_URL]).
 *
 * @return Готовый HTTP-клиент.
 */
fun httpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url(BuildKonfig.BASE_URL)
        }
    }
}