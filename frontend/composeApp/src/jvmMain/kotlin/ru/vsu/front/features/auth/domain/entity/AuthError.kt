@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.domain.entity

/**
 * @see AuthError Класс ошибки при аутентификации
 *
 * @param message Сообщение об ошибке
 */
sealed class AuthError(open val message: String) {
    /**
     * @see Forbidden Класс ошибки 403
     */
    data class Forbidden(override val message: String) : AuthError(message)
    /**
     * @see BadRequest Класс ошибки 400
     */
    data class BadRequest(override val message: String) : AuthError(message)
    /**
     * @see NetworkException Ошибка интернет соединения
     */
    data class NetworkException(override val message: String = "Network Error") : AuthError(message)
    /**
     * @see UnknownError Неизвестная ошибка
     */
    data class UnknownError(override val message: String = "Unknown Error") : AuthError(message)
}