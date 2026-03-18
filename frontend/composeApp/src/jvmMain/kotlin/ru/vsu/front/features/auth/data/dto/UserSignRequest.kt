package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на регистрацию пользователя
 *
 * @param name Имя пользователя
 * @param email Почта пользователя
 * @param password Пароль пользователя
 */
@Serializable
data class UserSignRequest(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)