package ru.vsu.front.features.auth.domain.repository

import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession

/**
 * Интерфейс репозитория для аутентификации и регистрации.
 */
interface AuthRepository {

    /**
     * Выполняет авторизацию.
     *
     * @param email Введенная почта пользователя.
     * @param password Введенный пароль пользователя.
     * @return [AuthResult] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend fun login(email: String, password: String): AuthResult<UserSession>

    /**
     * Выполняет регистрацию пользователя.
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return [AuthResult] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend fun sign(name: String, email: String, password: String): AuthResult<UserSession>
}