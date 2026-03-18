package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на логин пользователя
 *
 * @param email Почта пользователя
 * @param password Пароль пользователя
 */
@Serializable
data class UserLoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)