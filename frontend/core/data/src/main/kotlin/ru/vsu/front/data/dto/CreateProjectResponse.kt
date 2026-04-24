package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO ответа на запрос создания проекта.
 *
 * @property projectId Идентификатор проекта.
 */
@Serializable
data class CreateProjectResponse(
    @SerialName("project_id") val projectId: Int
)