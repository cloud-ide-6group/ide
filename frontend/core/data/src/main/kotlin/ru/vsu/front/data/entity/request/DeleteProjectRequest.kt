package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteProjectRequest(
    @SerialName("project_id") val projectId: Int
)
