package ru.vsu.front.domain.di

import org.koin.dsl.module
import ru.vsu.front.domain.usecase.LoginUseCase
import ru.vsu.front.domain.usecase.RefreshUseCase
import ru.vsu.front.domain.usecase.SignUseCase

/**
 * Модуль слоя domain.
 * * Предоставляет бизнес-логику приложения.
 * * Что внутри:
 * - [LoginUseCase] - Авторизация пользователя (вход в аккаунт).
 * - [SignUseCase] - Регистрация пользователя.
 * - [RefreshUseCase] - Обновление токенов.
 */
val domainModule = module {
    single {
        LoginUseCase(get())
    }

    single {
        SignUseCase(get())
    }

    single {
        RefreshUseCase(get())
    }
}