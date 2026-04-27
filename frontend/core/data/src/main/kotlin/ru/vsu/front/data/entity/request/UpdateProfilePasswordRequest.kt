package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на обновление пароля.
 *
 * @property newPassword Новый пароль.
 * @property oldPassword Старый пароль
 */
@Serializable
data class UpdateProfilePasswordRequest(
    @SerialName("new_password") val newPassword: String,
    @SerialName("old_password") val oldPassword: String
)