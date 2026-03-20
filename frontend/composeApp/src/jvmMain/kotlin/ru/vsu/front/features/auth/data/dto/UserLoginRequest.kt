package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO тела POST-запроса на авторизацию пользователя.
 *
 * @property email Введенная почта пользователя.
 * @property password Не зашифрованный пароль пользователя.
 */
@Serializable
data class UserLoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)