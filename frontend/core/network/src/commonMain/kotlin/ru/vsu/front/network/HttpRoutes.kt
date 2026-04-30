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
}