package ru.vsu.front.model.entity

/**
 * Текущая сессия авторизованного пользователя.
 *
 * @property user Пользователь.
 * @property tokens JWT токены.
 */
data class UserData(
    val user: User,
    val tokens: AuthTokens
)