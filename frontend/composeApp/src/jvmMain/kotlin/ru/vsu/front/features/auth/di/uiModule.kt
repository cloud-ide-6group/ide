package ru.vsu.front.features.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.features.auth.ui.AuthViewModel

/**
 * Модуль слоя ui.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [AuthViewModel] - вьюмодель экрана логина и регистрации.
 */
val uiModule = module {
    viewModel { AuthViewModel(get(), get(), get(), get()) }
}