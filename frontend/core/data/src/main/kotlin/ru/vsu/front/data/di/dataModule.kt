package ru.vsu.front.data.di

import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.data.DefaultSocketHandler
import ru.vsu.front.data.repository.DefaultAuthRepository
import ru.vsu.front.data.repository.DefaultChatRepository
import ru.vsu.front.data.repository.DefaultFileRepository
import ru.vsu.front.data.repository.DefaultNotificationsRepository
import ru.vsu.front.data.repository.DefaultPremiumRepository
import ru.vsu.front.data.repository.DefaultProfileRepository
import ru.vsu.front.data.repository.DefaultProgramingLanguageRepository
import ru.vsu.front.data.repository.DefaultProjectRepository
import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.domain.repository.ChatRepository
import ru.vsu.front.domain.repository.FileRepository
import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.domain.repository.PremiumRepository
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.domain.repository.ProgramingLanguageRepository
import ru.vsu.front.domain.repository.ProjectRepository
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
        DefaultPremiumRepository(get())
    }.bind<PremiumRepository>()

    single {
        DefaultSocketHandler(get(), get(named("baseUrl")))
    }.bind<SocketHandler>()
}