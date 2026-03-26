package ru.vsu.front.network.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vsu.front.network.httpClient

/**
 * Модуль слоя network.
 *
 * * Отвечает за предоставление зависимостей для работы с сетью.
 * * Что внутри:
 * - `HttpClient` - Ktor-клиент через функцию [httpClient].
 */
val networkModule = module {
    single {
        httpClient(baseUrl = get(named("baseUrl")))
    }
}