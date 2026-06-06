package ru.vsu.front.data.di

import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.data.DefaultSocketHandler
import ru.vsu.front.data.repository.*
import ru.vsu.front.domain.repository.*
import ru.vsu.front.domain.socket.SocketHandler

/**
 * Модуль слоя data.
 * .
 * * Что внутри:
 * - [AuthRepository] - реализация репозитория через [DefaultAuthRepository].
 * - [ProfileRepository] - реализация репозитория через [DefaultProfileRepository].
 * - [ProgramingLanguageRepository] - реализация репозитория через [DefaultProgramingLanguageRepository].
 * - [DefaultProjectRepository] - реализация репозитория через [ProjectRepository].
 * - [DefaultNotificationsRepository] - реализация репозитория через [NotificationsRepository].
 * - [DefaultFileRepository] - реализация репозитория через [FileRepository].
 * - [DefaultChatRepository] - реализация репозитория через [ChatRepository].
 * - [DefaultPremiumRepository] - реализация репозитория через [PremiumRepository].
 * - [DefaultSocketHandler] - реализация интерфейса [SocketHandler].
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
        DefaultProjectRepository(get(), get())
    }.bind<ProjectRepository>()

    single {
        DefaultNotificationsRepository(get(), get(), get(named("baseUrl")))
    }.bind<NotificationsRepository>()

    single {
        DefaultFileRepository(get())
    }.bind<FileRepository>()

    single {
        DefaultChatRepository(get())
    }.bind<ChatRepository>()

    single {
        DefaultPremiumRepository(get(),get(), get(named("baseUrl")))
    }.bind<PremiumRepository>()

    single {
        DefaultSocketHandler(get(), get(named("baseUrl")))
    }.bind<SocketHandler>()
}