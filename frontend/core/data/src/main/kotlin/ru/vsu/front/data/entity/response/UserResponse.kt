package ru.vsu.front.data.entity.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ на запрос приглашения пользователя.
 *
 * @property userId Идентификатор пользователя.
 * @property name Имя пользователя.
 * @property email Почта пользователя
 */
@Serializable
data class UserResponse(
    @SerialName("id") val userId: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String
)