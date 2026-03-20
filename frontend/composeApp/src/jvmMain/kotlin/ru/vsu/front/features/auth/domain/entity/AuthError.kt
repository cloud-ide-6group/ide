package ru.vsu.front.features.auth.domain.entity

/**
 * Базовый класс для представления ошибок, возникающих при аутентификации.
 *
 * @property message Сообщение об ошибке.
 */
sealed class AuthError(open val message: String) {
    /**
     * 403: Доступ запрещен.
     * */
    data class Forbidden(override val message: String) : AuthError(message)

    /**
     * 400: Неверный запрос.
     * */
    data class BadRequest(override val message: String) : AuthError(message)

    /**
     * Ошибка сети (отсутствует интернет или сервер недоступен).
     * */
    data class NetworkException(override val message: String = "Network Error") : AuthError(message)

    /** 
     * Непредвиденная ошибка.
     * */
    data class UnknownError(override val message: String = "Unknown Error") : AuthError(message)
}