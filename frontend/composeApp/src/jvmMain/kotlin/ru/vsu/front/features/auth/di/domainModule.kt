@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.di

import org.koin.dsl.module
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase
import ru.vsu.front.features.auth.domain.usecase.SignUseCase

/**
 * Модуль слоя domain
 *
 * @see LoginUseCase Предоставление юзкейса логина, Singletone
 * @see SignUseCase Предоставление юзкейса регистрации, Singletone
 */
val domainModule = module {
    single {
        LoginUseCase(get())
    }

    single {
        SignUseCase(get())
    }
}