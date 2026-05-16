package ru.vsu.front.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vsu.front.config.BuildKonfig
import ru.vsu.front.settings.Settings

/**
 * Модуль приложения.
 *
 * * Что внутри:
 * - [BuildKonfig.BASE_URL] - базовый url для запросов.
 * - [Settings] - настройки приложения.
 */
val appModule = module {
    single(named("baseUrl")) {
        BuildKonfig.BASE_URL
    }
    single {
        Settings(get())
    }
}