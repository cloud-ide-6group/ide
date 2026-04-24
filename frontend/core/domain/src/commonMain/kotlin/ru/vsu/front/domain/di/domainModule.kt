package ru.vsu.front.domain.di

import org.koin.dsl.module
import ru.vsu.front.domain.usecase.GetProfileUseCase
import ru.vsu.front.domain.usecase.GetProgramingLanguagesUseCase
import ru.vsu.front.domain.usecase.LoginUseCase
import ru.vsu.front.domain.usecase.CreateProjectUseCase
import ru.vsu.front.domain.usecase.RefreshUseCase
import ru.vsu.front.domain.usecase.SignUseCase
import ru.vsu.front.domain.usecase.UpdateProfileDataUseCase
import ru.vsu.front.domain.usecase.UpdateProfilePasswordUseCase
import ru.vsu.front.domain.usecase.UpdateProfilePhotoUseCase

/**
 * Модуль слоя domain.
 * * Предоставляет бизнес-логику приложения.
 * * Что внутри:
 * - [LoginUseCase] - Авторизация пользователя (вход в аккаунт).
 * - [SignUseCase] - Регистрация пользователя.
 * - [RefreshUseCase] - Обновление токенов.
 * - [GetProfileUseCase] - Получение профиля пользователя.
 * - [GetProgramingLanguagesUseCase] - Получение доступных языков программирования.
 * - [CreateProjectUseCase] - Создание проекта.
 * - [UpdateProfileDataUseCase] - Обновление имени и почты пользователя.
 * - [UpdateProfilePasswordUseCase] - Обновление пароля пользователя.
 * - [UpdateProfilePhotoUseCase] - Обновление аватара пользователя.
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

    single {
        GetProfileUseCase(get())
    }

    single {
        GetProgramingLanguagesUseCase(get())
    }

    single {
        CreateProjectUseCase(get())
    }

    single {
        UpdateProfileDataUseCase(get())
    }

    single {
        UpdateProfilePasswordUseCase(get())
    }

    single {
        UpdateProfilePhotoUseCase(get())
    }
}