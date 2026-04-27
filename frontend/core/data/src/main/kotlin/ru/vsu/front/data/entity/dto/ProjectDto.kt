package ru.vsu.front.data.entity.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO проекта.
 *
 * @property id Идентификатор проекта.
 * @property name Название проекта.
 */
@Serializable
data class ProjectDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)
