package ru.vsu.front.profile.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.profile.ProfileViewModel

/**
 * Модуль профиля.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [ProfileViewModel] - вьюмодель экрана профиля.
 */
val profileModule = module {
    viewModel { (userId: Int) ->
        ProfileViewModel(userId = userId, get(), get())
    }
}