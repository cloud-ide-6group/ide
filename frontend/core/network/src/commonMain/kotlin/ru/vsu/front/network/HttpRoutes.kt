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
     * Эндпоинт удаления проекта.
     */
    const val DELETE_PROJECT = "/project/create"

    /**
     * Эндпоинт обновления профиля.
     */
    const val UPDATE_PROFILE = "/profile/update"
}