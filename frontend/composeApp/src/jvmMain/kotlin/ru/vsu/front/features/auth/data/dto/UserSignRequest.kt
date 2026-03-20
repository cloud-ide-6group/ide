package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO тела POST-запроса на регистрацию нового пользователя.
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