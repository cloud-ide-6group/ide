package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос создание чата.
 *
 * @property projectId Идентификатор проекта.
 * @property identificator Идентификатор чата.
 */
@Serializable
data class CreateChatRequest(
    @SerialName("project_id") val projectId: Int,
    @SerialName("identificator") val identificator: String,
)