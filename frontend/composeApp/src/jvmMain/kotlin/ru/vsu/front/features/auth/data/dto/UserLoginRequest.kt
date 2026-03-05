@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.Serializable

/**
 * Запрос на логин пользователя
 *
 * @param email Почта пользователя
 * @param password Пароль пользователя
 */
@Serializable
data class UserLoginRequest(
    val email: String,
    val password: String
)