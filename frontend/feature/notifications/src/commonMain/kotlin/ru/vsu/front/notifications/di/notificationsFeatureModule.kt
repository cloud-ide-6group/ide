package ru.vsu.front.notifications.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.vsu.front.notifications.NotificationsViewModel

/**
 * Модуль уведомлений.
 * * Отвечает за предоставление ViewModel для экранов.
 * * Что внутри:
 * - [NotificationsViewModel] - вьюмодель экрана уведомлений.
 */
val notificationsModule = module {
    viewModel {
        NotificationsViewModel(get(), get(), get())
    }
}