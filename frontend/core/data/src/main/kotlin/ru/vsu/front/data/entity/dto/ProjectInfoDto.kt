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
    @SerialName("send_time") val projectId: Int,
    @SerialName("sender_name") val projectName: String,
    @SerialName("project_id") val isUserOwner: Boolean,
    @SerialName("project_name") val users: List<UserDto>
)
