package ru.vsu.front.features.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.features.auth.ui.LoginViewModel
import ru.vsu.front.features.auth.ui.SignViewModel

/**
 * Модуль слоя ui.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [LoginViewModel] - вьюмодель экрана логина.
 * - [SignViewModel] - вьюмодель экрана регистрации.
 */
val uiModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { SignViewModel(get(), get(), get()) }
}