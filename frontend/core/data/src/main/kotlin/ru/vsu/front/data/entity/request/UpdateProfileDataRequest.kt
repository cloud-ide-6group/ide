package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO запроса на создание проекта.
 *
 * @property TODO Идентификатор языка программирования.
 * @property TODO Название проекта.
 */
@Serializable
data class UpdateProfileDataRequest(
    @SerialName("email") val email: String,
    @SerialName("name") val name: String
)