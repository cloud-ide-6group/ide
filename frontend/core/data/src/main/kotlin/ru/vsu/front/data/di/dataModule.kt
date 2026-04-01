package ru.vsu.front.data.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.data.repository.DefaultAuthRepository
import ru.vsu.front.data.repository.DefaultProfileRepository
import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.domain.repository.ProfileRepository

/**
 * Модуль слоя data.
 * .
 * * Что внутри:
 * - [AuthRepository] - реализация репозитория через [DefaultAuthRepository].
 * - [ProfileRepository] - реализация репозитория через [DefaultProfileRepository].
 */
val dataModule = module {
    single {
        DefaultAuthRepository(get())
    }.bind<AuthRepository>()

    single {
        DefaultProfileRepository(get(), get())
    }.bind<ProfileRepository>()
}