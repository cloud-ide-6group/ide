package ru.vsu.front.network.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vsu.front.network.MainHttpClientManager
import ru.vsu.front.network.baseHttpClient

/**
 * Модуль слоя network.
 *
 * * Отвечает за предоставление зависимостей для работы с сетью.
 * * Что внутри:
 * - [baseHttpClient] - Ktor-клиент для выполнения запросов связанных с аутентификацией
 * (в заголовке не отправляются токены).
 * - [MainHttpClientManager] - Менеджер Ktor-клиента для выполнения запросов, в заголовке которых может быть токен
 * (реализовано автоматическое обновление токенов при получении ошибки 401).
 */
val networkModule = module {
    single(named("authHttpClient")) {
        baseHttpClient(baseUrl = get(named("baseUrl")))
    }
    single {
        MainHttpClientManager(baseUrl = get(named("baseUrl")), get(), get(), get())
    }
}