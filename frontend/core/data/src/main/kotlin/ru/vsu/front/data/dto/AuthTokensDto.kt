package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO JWT токенов.
 *
 * @property accessToken Короткоживущий токен доступа.
 * @property refreshToken Долгоживущий токен для обновления [accessToken].
 */
@Serializable
data class AuthTokensDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)