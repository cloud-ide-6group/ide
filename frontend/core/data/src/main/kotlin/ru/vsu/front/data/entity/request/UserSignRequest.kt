package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на регистрацию нового пользователя.
 *
 * @property name Имя пользователя.
 * @property email Почта пользователя.
 * @property password Не зашифрованный пароль.
 */
@Serializable
data class UserSignRequest(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)