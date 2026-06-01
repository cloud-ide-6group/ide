package ru.vsu.front.data.entity.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO информации о проекте.
 *
 * @property languageName Язык программирования в проекте.
 * @property projectId Идентификатор проекта.
 * @property projectName Название проекта.
 * @property isUserOwner Является ли пользователь владельцем
 * @property users Список участников проекта
 */
@Serializable
data class ProjectInfoDto(
    @SerialName("language_name") val languageName: String,
    @SerialName("project_id") val projectId: Int,
    @SerialName("project_name") val projectName: String,
    @SerialName("user_is_owner") val isUserOwner: Boolean,
    @SerialName("users") val users: List<UserDto>
)
