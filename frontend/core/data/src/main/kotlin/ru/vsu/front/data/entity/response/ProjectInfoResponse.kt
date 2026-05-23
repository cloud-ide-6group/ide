package ru.vsu.front.data.entity.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vsu.front.data.entity.dto.ProjectInfoDto

@Serializable
class ProjectInfoResponse(
    @SerialName("project_info") val projectInfoDto: ProjectInfoDto
)
