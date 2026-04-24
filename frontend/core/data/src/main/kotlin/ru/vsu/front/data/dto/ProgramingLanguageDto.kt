package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO языка программирования.
 *
 * @property id Идентификатор языка программирования.
 * @property name Название языка программирования.
 * @property description Описание языка программирования.
 */
@Serializable
data class ProgramingLanguageDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String
)
