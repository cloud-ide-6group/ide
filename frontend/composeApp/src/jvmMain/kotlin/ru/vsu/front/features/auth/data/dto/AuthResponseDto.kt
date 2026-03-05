@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ сервера при аутентификации
 *
 * @param name Имя пользователя
 * @param email Почта пользователя
 * @param photoPath Путь до аватара пользователя [На сервере]
 * @param accessToken Токен доступа
 * @param refreshToken Токен обновления
 */
@Serializable
data class AuthResponseDto(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("photo_path") val photoPath: String,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
)