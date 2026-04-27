package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на создание проекта.
 *
 * @property programingLanguageId Идентификатор языка программирования.
 * @property projectName Название проекта.
 */
@Serializable
data class CreateProjectRequest(
    @SerialName("language_id") val programingLanguageId: Int,
    @SerialName("project_name") val projectName: String
)