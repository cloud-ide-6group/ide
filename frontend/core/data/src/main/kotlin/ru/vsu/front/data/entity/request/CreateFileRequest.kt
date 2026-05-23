package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос создание файла.
 *
 * @property fileName Название файла.
 * @property projectId Идентификатор проекта.
 * @property isFolder Является ли папкой создаваемый файл.
 * @property parentId Идентификатор файла-родителя.
 */
@Serializable
data class CreateFileRequest(
    @SerialName("name") val fileName: String,
    @SerialName("project_id") val projectId: Int,
    @SerialName("is_folder") val isFolder: Boolean,
    @SerialName("parent_id") val parentId: Int?
)
