package ru.vsu.front.data.entity.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ на запрос создания проекта при его успешном создании.
 *
 * @property projectId Идентификатор проекта.
 */
@Serializable
data class CreateProjectResponse(
    @SerialName("project_id") val projectId: Int
)