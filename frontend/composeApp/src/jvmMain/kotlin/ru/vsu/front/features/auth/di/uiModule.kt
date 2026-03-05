package ru.vsu.front.features.auth.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.vsu.front.features.auth.ui.LoginViewModel

val uiModule = module {
    viewModelOf(::LoginViewModel)
}