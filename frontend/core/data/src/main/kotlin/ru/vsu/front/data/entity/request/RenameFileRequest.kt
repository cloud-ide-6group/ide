package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос удаления файла.
 *
 * @property id Идентификатор файла.
 * @property newName Новое название.
 */
@Serializable
data class RenameFileRequest(
    @SerialName("file_id") val id: Int,
    @SerialName("new_name") val newName: String
)
