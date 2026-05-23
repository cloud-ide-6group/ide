package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос удаления файла.
 *
 * @property fileId Идентификатор файла.
 */
@Serializable
data class DeleteFileRequest(
    @SerialName("file_id") val fileId: Int
)
