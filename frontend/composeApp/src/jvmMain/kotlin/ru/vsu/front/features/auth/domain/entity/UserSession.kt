package ru.vsu.front.features.auth.domain.entity

/**
 * Текущая сессия авторизованного пользователя.
 *
 * @property user Пользователь.
 * @property tokens JWT токены.
 */
data class UserSession(
    val user: User,
    val tokens: AuthTokens
)