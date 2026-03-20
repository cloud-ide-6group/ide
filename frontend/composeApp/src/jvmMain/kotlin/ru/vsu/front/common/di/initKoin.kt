package ru.vsu.front.common

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration
import ru.vsu.front.common.security.di.appModule
import ru.vsu.front.features.auth.di.dataModule
import ru.vsu.front.features.auth.di.domainModule
import ru.vsu.front.features.auth.di.uiModule

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, uiModule, appModule)
    }
}