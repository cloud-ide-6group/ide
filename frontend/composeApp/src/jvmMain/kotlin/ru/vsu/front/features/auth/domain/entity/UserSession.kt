@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.domain.entity

/**
 * Сессия пользователя
 *
 * @param user Пользователь
 * @param tokens Токены пользователя
 */
data class UserSession(
    val user: User,
    val tokens: AuthTokens
)