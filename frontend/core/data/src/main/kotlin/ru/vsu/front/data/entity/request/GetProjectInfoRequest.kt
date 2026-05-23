package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос получения информации о проекте.
 *
 * @property projectId Идентификатор проекта.
 */
@Serializable
data class GetProjectInfoRequest(
    @SerialName("project_id") val projectId: Int
)
