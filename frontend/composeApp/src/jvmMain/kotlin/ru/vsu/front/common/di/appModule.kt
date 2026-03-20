package ru.vsu.front.common.di

import org.koin.dsl.module
import ru.vsu.front.common.di.dispatcher_provider.DefaultDispatcherProvider
import ru.vsu.front.common.di.dispatcher_provider.DispatcherProvider

/**
 * Модуль приложения
 *
 * @see DefaultDispatcherProvider Предоставление дефолтных диспатчеров
 */
val appModule = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
}