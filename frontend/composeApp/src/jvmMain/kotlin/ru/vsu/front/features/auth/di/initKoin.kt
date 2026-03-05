package ru.vsu.front.features.auth.di

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, uiModule)
    }
}