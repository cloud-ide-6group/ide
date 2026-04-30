package ru.vsu.front.auth.di

import org.koin.dsl.module
import ru.vsu.front.auth.AuthManager

/**
 * Модуль аутентификации.
 *
 * * Что внутри:
 * - [AuthManager] - Менеджер аутентификации.
 */
val authModule = module {
    single {
        AuthManager(get())
    }
}