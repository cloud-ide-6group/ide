package ru.vsu.front.common.di

import org.koin.dsl.module
import ru.vsu.front.common.dispatcher_provider.DefaultDispatcherProvider
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider

/**
 * Модуль общей логики.
 *
 * * Что внутри:
 * - [DispatcherProvider] - реализация по умолчанию через [DefaultDispatcherProvider].
 */
val commonModule = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
}