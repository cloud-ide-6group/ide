package ru.vsu.front.data.dto

import kotlinx.serialization.Serializable

/**
 * DTO проекта.
 *
 * @property id Идентификатор проекта.
 * @property name Название проекта.
 * @property ownerId Идентификатор владельца проекта.
 * @property programmingLanguageId Используемый язык программирование в проекте.
 */
@Serializable
data class ProjectDto(
    val id: Int,
    val name: String,
    val ownerId: Int,
    val programmingLanguageId: Int
)
