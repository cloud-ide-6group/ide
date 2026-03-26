package ru.vsu.front.authorization.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.authorization.AuthViewModel

/**
 * Модуль аутентификации.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [AuthViewModel] - вьюмодель экрана логина и регистрации.
 */
val authorizationModule = module {
    viewModel { AuthViewModel(get(), get(), get(), get()) }
}