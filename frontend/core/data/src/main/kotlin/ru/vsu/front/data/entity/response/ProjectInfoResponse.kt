package ru.vsu.front.data.entity.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vsu.front.data.entity.dto.ProjectInfoDto

/**
 * Ответ на запрос получения информации о проекте.
 *
 * @property projectInfoDto Информация о проекте.
 */
@Serializable
class ProjectInfoResponse(
    @SerialName("project_info") val projectInfoDto: ProjectInfoDto
)
