package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO запроса на создание проекта.
 *
 * @property TODO Идентификатор языка программирования.
 */
@Serializable
data class UpdateProfilePhotoRequest(
    @SerialName("photo") val photoBase64: String,
)