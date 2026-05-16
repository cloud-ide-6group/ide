package ru.vsu.front.di

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.KoinAppDeclaration
import ru.vsu.front.auth.di.authModule
import ru.vsu.front.authorization.di.authorizationModule
import ru.vsu.front.common.di.commonModule
import ru.vsu.front.data.di.dataModule
import ru.vsu.front.datastore.di.datastoreModule
import ru.vsu.front.domain.di.domainModule
import ru.vsu.front.network.di.networkModule
import ru.vsu.front.notifications.di.notificationsModule
import ru.vsu.front.notifications.di.projectModule
import ru.vsu.front.profile.di.profileModule

/**
 * Инициализирует граф зависимостей Koin для всего приложения.
 * Собирает вместе все необходимые модули.
 *
 * @param config Конфигурация Koin.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            dataModule,
            domainModule,
            datastoreModule,
            commonModule,
            networkModule,
            appModule,
            authModule,
            authorizationModule,
            profileModule,
            notificationsModule,
            settingsModule,
            projectModule
        )
    }
}