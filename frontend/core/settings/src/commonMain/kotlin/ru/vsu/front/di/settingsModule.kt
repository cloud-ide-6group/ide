package ru.vsu.front.di

import org.koin.dsl.module
import ru.vsu.front.ThemeSettings

/**
 * Модуль слоя settings.
 *
 * * Отвечает за предоставление зависимостей для работы с настройками.
 * * Что внутри:
 * - [ThemeSettings] - Настройки темы.
 */
val settingsModule = module {
    single {
        ThemeSettings()
    }
}