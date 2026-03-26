package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO сессии пользователя.
 *
 * @property name Имя пользователя.
 * @property email Почта пользователя.
 * @property photoPath Путь до аватара пользователя на сервере.
 * @property accessToken Короткоживущий JWT токен доступа.
 * @property refreshToken Долгоживущий JWT токен обновления.
 */
@Serializable
data class UserDataDto(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("photo_path") val photoPath: String,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
)