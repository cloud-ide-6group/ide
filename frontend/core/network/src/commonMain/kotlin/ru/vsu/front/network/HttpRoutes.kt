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
}