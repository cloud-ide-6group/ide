package ru.vsu.front.network

/**
 * Константы с эндпоинтами API для сетевых запросов к бэкенду.
 */
object HttpRoutes {
    /**
     * Эндпоинт авторизации (вход в аккаунт).
     */
    const val LOGIN = "/login"

    /**
     * Эндпоинт регистрации.
     */
    const val SIGN = "/sign"

    /**
     * Эндпоинт обновления токенов.
     */
    const val REFRESH_TOKENS = "/refresh"

    /**
     * Эндпоинт получения профиля.
     */
    const val PROFILE = "/profile"

    /**
     * Эндпоинт создания проекта.
     */
    const val CREATE_PROJECT = "/project/create"

    /**
     * Эндпоинт получения доступных языков программирования.
     */
    const val PROGRAMING_LANGUAGES = "/languages"

    /**
     * Эндпоинт обновления почты и логина пользователя.
     */
    const val UPDATE_PROFILE_DATA = "/profile/update/data"

    /**
     * Эндпоинт обновления пароля.
     */
    const val UPDATE_PROFILE_PASSWORD = "/profile/update/password"

    /**
     * Эндпоинт обновления аватара.
     */
    const val UPDATE_PROFILE_PHOTO = "/profile/update/photo"

    /**
     * Эндпоинт удаления уведомления.
     */
    const val DELETE_NOTIFICATION = "/notification/delete"

    /**
     * Эндпоинт создания файла.
     */
    const val CREATE_FILE = "/files/create"

    /**
     * Эндпоинт удаления файла.
     */
    const val DELETE_FILE = "/files/delete"

    /**
     * Эндпоинт переименования файла.
     */
    const val RENAME_FILE = "/files/rename"

    /**
     * Эндпоинт создания чата.
     */
    const val CREATE_CHAT = "/chat/create"

    /**
     * Эндпоинт создания сообщения.
     */
    const val CREATE_MESSAGE = "/message/create"

    /**
     * Эндпоинт получения информации о проекте.
     */
    const val GET_PROJECT_INFO = "/project/info"

    /**
     * Эндпоинт удаления проекта.
     */
    const val DELETE_PROJECT = "/project/delete"

    /**
     * Эндпоинт исключения участника из проекта.
     */
    const val KICK_USER = "/invited/delete"

    /**
     * Эндпоинт приглашения пользователя в проект.
     */
    const val INVITE_USER = "/invite"
}