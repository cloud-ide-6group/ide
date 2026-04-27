package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на обновление аватара.
 *
 * @property photoBase64 Фото в формате Base64 (String).
 */
@Serializable
data class UpdateProfilePhotoRequest(
    @SerialName("photo") val photoBase64: String,
)