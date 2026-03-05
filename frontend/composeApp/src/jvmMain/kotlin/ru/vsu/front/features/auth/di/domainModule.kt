package ru.vsu.front.features.auth.di

import org.koin.dsl.module
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase
import ru.vsu.front.features.auth.domain.usecase.SignUseCase

val domainModule = module {
    single {
        LoginUseCase(get())
    }

    single {
        SignUseCase(get())
    }
}