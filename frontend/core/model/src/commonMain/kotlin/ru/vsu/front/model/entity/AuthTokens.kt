package ru.vsu.front.model.entity

/**
 * JWT токены.
 *
 * @property accessToken Короткоживущий токен доступа.
 * @property refreshToken Долгоживущий токен для обновления [accessToken].
 */
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)