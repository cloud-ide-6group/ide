package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос удаления проекта.
 *
 * @property projectId Идентификатор проекта.
 */
@Serializable
data class DeleteProjectRequest(
    @SerialName("project_id") val projectId: Int
)
