package ru.vsu.front.domain.di

import org.koin.dsl.module
import ru.vsu.front.domain.usecase.*

/**
 * Модуль слоя domain.
 * * Предоставляет бизнес-логику приложения.
 * * Что внутри:
 * - [LoginUseCase] - Авторизация пользователя (вход в аккаунт).
 * - [SignUseCase] - Регистрация пользователя.
 * - [RefreshUseCase] - Обновление токенов.
 * - [GetProfileUseCase] - Получение профиля пользователя.
 * - [GetProgramingLanguagesUseCase] - Получение доступных языков программирования.
 * - [CreateProjectUseCase] - Создание проекта.
 * - [UpdateProfileDataUseCase] - Обновление имени и почты пользователя.
 * - [UpdateProfilePasswordUseCase] - Обновление пароля пользователя.
 * - [UpdateProfilePhotoUseCase] - Обновление аватара пользователя.
 * - [ObserveNotificationsUseCase] - Подписка на уведомления.
 * - [DeleteNotificationUseCase] - Удаление уведомления.
 * - [ObserveNotificationsUseCase] - Подписка на файлы проекта.
 * - [ConnectToTheProjectRoomUseCase] - Подключение к комнате проекта.
 * - [CreateFileUseCase] - Создание файла.
 * - [DeleteFileUseCase] - Удаление файла.
 * - [RenameFileUseCase] - Переименовывание файла.
 * - [ObserveFileContentUseCase] - Подписка на содержимое файла.
 * - [UpdateFileContentUseCase] - Обновление содержимого файла.
 * - [RunCodeUseCase] - Запуск программы.
 * - [StopCodeUseCase] - Остановка выполнения программа.
 * - [ObserveConsoleOutputUseCase] - Подписка на результат запуска кода.
 * - [SendInputUseCase] Отправка ввода (строки) в текущую выполняемую программу.
 * - [CreateChatUseCase] Создание чата.
 * - [JoinChatUseCase] Подключение к чату.
 * - [LeaveChatUseCase] Отключение от чата.
 * - [ObserveMessagesUseCase] Подписка на сообщения текущего чата.
 * - [CreateMessageUseCase] Создание сообщения (отправка).
 * - [ObserveRemovedFromProjectUseCase] Подписка на событие исключения текущего пользователя из проекта.
 * - [LeaveFromProjectRoomUseCase] Отключение от комнаты проекта.
 */
val domainModule = module {
    single {
        LoginUseCase(get())
    }

    single {
        SignUseCase(get())
    }

    single {
        RefreshUseCase(get())
    }

    single {
        GetProfileUseCase(get())
    }

    single {
        GetProgramingLanguagesUseCase(get())
    }

    single {
        CreateProjectUseCase(get())
    }

    single {
        UpdateProfileDataUseCase(get())
    }

    single {
        UpdateProfilePasswordUseCase(get())
    }

    single {
        UpdateProfilePhotoUseCase(get())
    }

    single {
        ObserveNotificationsUseCase(get())
    }

    single {
        DeleteNotificationUseCase(get())
    }

    single {
        ObserveFilesUseCase(get())
    }

    single {
        ConnectToTheProjectRoomUseCase(get())
    }

    single {
        CreateFileUseCase(get())
    }

    single {
        DeleteFileUseCase(get())
    }

    single {
        RenameFileUseCase(get())
    }

    single {
        ObserveFileContentUseCase(get())
    }

    single {
        UpdateFileContentUseCase(get())
    }

    single {
        RunCodeUseCase(get())
    }

    single {
        StopCodeUseCase(get())
    }

    single {
        ObserveConsoleOutputUseCase(get())
    }

    single {
        SendInputUseCase(get())
    }

    single {
        CreateChatUseCase(get())
    }

    single {
        JoinChatUseCase(get())
    }

    single {
        LeaveChatUseCase(get())
    }

    single {
        ObserveMessagesUseCase(get())
    }

    single {
        CreateMessageUseCase(get())
    }

    single {
        ObserveRemovedFromProjectUseCase(get())
    }

    single {
        LeaveFromProjectRoomUseCase(get())
    }
}