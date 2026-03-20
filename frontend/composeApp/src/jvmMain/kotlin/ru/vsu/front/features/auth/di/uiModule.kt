package ru.vsu.front.features.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.front.features.auth.ui.LoginViewModel
import ru.vsu.front.features.auth.ui.SignViewModel

/**
 * Модуль слоя ui
 *
 * @see LoginViewModel Предоставление вьюмодели экрана логина
 * @see SignViewModel Предоставление вьюмодели экрана регистрации
 */
val uiModule = module {
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { SignViewModel(get(), get(), get()) }
}