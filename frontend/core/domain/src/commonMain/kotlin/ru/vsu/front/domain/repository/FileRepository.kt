package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response

/**
 * Интерфейс репозитория для работы с файлами.
 */
interface FileRepository {

    /**
     * Выполняет создание файла.

     * @return [Response] с не важно чем при успехе (главное код ответа), либо с ошибкой.
     */
    suspend fun createFile(
        fileName: String,
        projectId: Int,
        isFolder: Boolean,
        parentId: Int?
    ): Response<*>

    /**
     * Выполняет удаление файла.

     * @return [Response] с не важно чем при успехе (главное код ответа), либо с ошибкой.
     */
    suspend fun deleteFile(
        fileId: Int,
    ): Response<*>

    /**
     * Выполняет переименовывание файла.

     * @return [Response] с не важно чем при успехе (главное код ответа), либо с ошибкой.
     */
    suspend fun renameFile(
        fileId: Int,
        newName: String,
    ): Response<*>
}