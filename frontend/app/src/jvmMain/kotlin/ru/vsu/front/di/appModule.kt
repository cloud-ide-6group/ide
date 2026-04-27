package ru.vsu.front.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vsu.front.config.BuildKonfig

/**
 * Модуль приложения.
 *
 * * Что внутри:
 * - [BuildKonfig.BASE_URL] - базовый url для запросов.
 */
val appModule = module {
    single(named("baseUrl")) {
        BuildKonfig.BASE_URL
    }
}