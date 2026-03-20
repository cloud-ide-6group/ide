package ru.vsu.front.features.auth.di

import org.koin.dsl.module
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase
import ru.vsu.front.features.auth.domain.usecase.SignUseCase

/**
 * Модуль слоя domain.
 * * Предоставляет бизнес-логику приложения.
 * * Что внутри:
 * - [LoginUseCase] - Авторизация пользователя (вход в аккаунт).
 * - [SignUseCase] - Регистрация пользователя.
 */
val domainModule = module {
    single {
        LoginUseCase(get())
    }

    single {
        SignUseCase(get())
    }
}