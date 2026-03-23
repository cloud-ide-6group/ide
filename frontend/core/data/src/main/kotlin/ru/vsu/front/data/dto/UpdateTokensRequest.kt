package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на обновление токенов.
 *
 * @property accessToken Короткоживущий токен доступа.
 * @property refreshToken Долгоживущий токен для обновления [accessToken].
 */
@Serializable
data class UpdateTokensRequest(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)