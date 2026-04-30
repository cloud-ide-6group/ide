package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на обновление токенов.
 *
 * @property refreshToken Долгоживущий токен для обновления токена доступа.
 */
@Serializable
data class UpdateTokensRequest(
    @SerialName("refresh_token") val refreshToken: String
)