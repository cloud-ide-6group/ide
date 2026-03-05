@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.front.features.auth.ui.LoginViewModel

/**
 * Модуль слоя ui
 *
 * @see LoginViewModel Предоставление вьюмодели экрана логина, Singletone
 */
val uiModule = module {
    viewModelOf(::LoginViewModel)
}