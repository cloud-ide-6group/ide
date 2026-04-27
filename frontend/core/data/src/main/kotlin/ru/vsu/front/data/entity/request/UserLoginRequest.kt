package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на авторизацию пользователя.
 *
 * @property email Почта пользователя.
 * @property password Не зашифрованный пароль пользователя.
 */
@Serializable
data class UserLoginRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)