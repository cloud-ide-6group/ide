package ru.vsu.front.features.auth.domain.entity

/**
 * JWT токены доступа и обновления
 *
 * @param accessToken Токен доступа
 * @param refreshToken Токен обновления
 */
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String
)