package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

/**
 * Интерфейс репозитория для аутентификации и регистрации.
 */
interface AuthRepository {

    /**
     * Выполняет авторизацию.
     *
     * @param email Введенная почта пользователя.
     * @param password Введенный пароль пользователя.
     * @return [Response] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend fun login(email: String, password: String): Response<AuthTokens>

    /**
     * Выполняет регистрацию пользователя.
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Пароль пользователя.
     * @return [Response] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend fun sign(name: String, email: String, password: String): Response<AuthTokens>

    /**
     * Выполняет обновление токенов.
     *
     * @param accessToken Токен доступа.
     * @param refreshToken Токен обновления.
     * @return [Response] с новыми токенами при успехе, либо с ошибкой.
     */
    suspend fun refresh(accessToken: String, refreshToken: String): Response<AuthTokens>
}