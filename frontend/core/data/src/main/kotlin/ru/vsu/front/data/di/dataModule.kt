package ru.vsu.front.data.di

import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.data.repository.DefaultAuthRepository
import ru.vsu.front.data.repository.DefaultNotificationsRepository
import ru.vsu.front.data.repository.DefaultProfileRepository
import ru.vsu.front.data.repository.DefaultProgramingLanguageRepository
import ru.vsu.front.data.repository.DefaultProjectRepository
import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.domain.repository.ProgramingLanguageRepository
import ru.vsu.front.domain.repository.ProjectRepository

/**
 * Модуль слоя data.
 * .
 * * Что внутри:
 * - [AuthRepository] - реализация репозитория через [DefaultAuthRepository].
 * - [ProfileRepository] - реализация репозитория через [DefaultProfileRepository].
 * - [ProgramingLanguageRepository] - реализация репозитория через [DefaultProgramingLanguageRepository].
 * - [DefaultProjectRepository] - реализация репозитория через [ProjectRepository].
 * - [DefaultNotificationsRepository] - реализация репозитория через [NotificationsRepository].
 */
val dataModule = module {
    single {
        DefaultAuthRepository(get(named("withoutJWTTokensHttpClient")))
    }.bind<AuthRepository>()

    single {
        DefaultProfileRepository(get(), get())
    }.bind<ProfileRepository>()

    single {
        DefaultProgramingLanguageRepository(get())
    }.bind<ProgramingLanguageRepository>()

    single {
        DefaultProjectRepository(get())
    }.bind<ProjectRepository>()

    single {
        DefaultNotificationsRepository(get(), get(named("baseUrl")))
    }.bind<NotificationsRepository>()
}