package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на обновление почты и имени пользователя.
 *
 * @property email Почта.
 * @property name Имя.
 */
@Serializable
data class UpdateProfileDataRequest(
    @SerialName("email") val email: String,
    @SerialName("name") val name: String
)