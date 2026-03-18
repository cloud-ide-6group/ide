package ru.vsu.front.features.auth.domain.repository

import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession

/**
 * Репозиторий, ответственный за авторизацию и регистрацию
 */
interface AuthRepository {
    /**
     * Функция авторизации
     *
     * @param email Почта пользователя
     * @param password Пароль пользователя
     */
    suspend fun login(email: String, password: String): AuthResult<UserSession>

    /**
     * Функция регистрации
     *
     * @param name Имя пользователя
     * @param email Почта пользователя
     * @param password Пароль пользователя
     */
    suspend fun sign(name: String, email: String, password: String): AuthResult<UserSession>
}