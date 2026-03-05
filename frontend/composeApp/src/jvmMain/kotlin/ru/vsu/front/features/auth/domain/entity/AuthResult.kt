@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.domain.entity

/**
 * Интерфейс ошибки
 *
 * @see Success Класс, представляющий собой положительный ответ сервера
 * @see Error Класс, представляющий собой отрицательный ответ сервера
 */
sealed interface AuthResult<out T> {
    /**
     * @param data Положительный результат
     */
    data class Success<T>(val data: T) : AuthResult<T>
    /**
     * @param errorData Отрицательный результат
     */
    data class Error<T>(val errorData: AuthError) : AuthResult<T>
}