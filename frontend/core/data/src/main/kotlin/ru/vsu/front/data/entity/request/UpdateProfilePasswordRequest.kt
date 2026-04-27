package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO запроса на создание проекта.
 *
 * @property TODO Идентификатор языка программирования.
 */
@Serializable
data class UpdateProfilePasswordRequest(
    @SerialName("new_password") val newPassword: String,
    @SerialName("old_password") val oldPassword: String
)