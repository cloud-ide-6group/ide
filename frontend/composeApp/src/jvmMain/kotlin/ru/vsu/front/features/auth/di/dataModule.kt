package ru.vsu.front.features.auth.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.features.auth.data.httpClient
import ru.vsu.front.features.auth.data.repository.AuthRepositoryImpl
import ru.vsu.front.features.auth.domain.repository.AuthRepository

val dataModule = module {
    single {
        AuthRepositoryImpl(get())
    }.bind<AuthRepository>()

    single {
        httpClient()
    }
}