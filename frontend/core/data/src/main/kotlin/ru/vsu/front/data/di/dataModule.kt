package ru.vsu.front.data.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.data.repository.DefaultAuthRepository
import ru.vsu.front.domain.repository.AuthRepository

/**
 * Модуль слоя data.
 * .
 * * Что внутри:
 * - [AuthRepository] - реализация репозитория через [DefaultAuthRepository].
 */
val dataModule = module {
    single {
        DefaultAuthRepository(get())
    }.bind<AuthRepository>()
}