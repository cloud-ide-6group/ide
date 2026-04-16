package ru.vsu.front.network.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vsu.front.network.authHttpClient
import ru.vsu.front.network.mainHttpClient

/**
 * Модуль слоя network.
 *
 * * Отвечает за предоставление зависимостей для работы с сетью.
 * * Что внутри:
 * - [authHttpClient] - Ktor-клиент для выполнения запросов связанных с аутентификацией
 * (в заголовке не отправляются токены).
 * - [mainHttpClient] - Ktor-клиент для выполнения запросов, в заголовке которых может быть токен
 * (реализовано автоматическое обновление токенов при получении ошибки 401).
 */
val networkModule = module {
    single(named("authHttpClient")) {
        authHttpClient(baseUrl = get(named("baseUrl")))
    }
    single(named("mainHttpClient")) {
        mainHttpClient(baseUrl = get(named("baseUrl")), get(), get())
    }
}