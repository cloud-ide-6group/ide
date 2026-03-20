package ru.vsu.front.features.auth.domain.entity

/**
 * Обертка для результата выполнения аутентификации.
 * Позволяет безопасно обрабатывать ответы.
 */
sealed interface AuthResult<out T> {

    /**
     * Представляет успешное выполнение.
     * @property data Какая-либо информация.
     */
    data class Success<T>(val data: T) : AuthResult<T>

    /**
     * Представляет ошибку выполнения.
     * @property errorData Ошибка аутентификации.
     */
    data class Error<T>(val errorData: AuthError) : AuthResult<T>
}