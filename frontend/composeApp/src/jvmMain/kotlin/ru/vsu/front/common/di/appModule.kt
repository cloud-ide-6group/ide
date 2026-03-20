package ru.vsu.front.common.di

import org.koin.dsl.module
import ru.vsu.front.common.di.dispatcher_provider.DefaultDispatcherProvider
import ru.vsu.front.common.di.dispatcher_provider.DispatcherProvider

/**
 * Базовый Koin-модуль приложения.
 * * Отвечает за предоставление глобальных зависимостей,
 * которые используются сквозь все слои архитектуры.
 * * Что внутри:
 * - [DispatcherProvider] - реализация по умолчанию через [DefaultDispatcherProvider].
 */
val appModule = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
}