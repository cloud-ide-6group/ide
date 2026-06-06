package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.data.entity.request.CreateFileRequest
import ru.vsu.front.data.entity.request.DeleteFileRequest
import ru.vsu.front.data.entity.request.RenameFileRequest
import ru.vsu.front.domain.repository.FileRepository
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.CREATE_FILE
import ru.vsu.front.network.HttpRoutes.DELETE_FILE
import ru.vsu.front.network.HttpRoutes.RENAME_FILE
import ru.vsu.front.network.MainHttpClientManager

/**
 * Реализация интерфейса [FileRepository] для работы с файловой системой проекта через сетевое API (HTTP-запросы).
 *
 * @property mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 */
class DefaultFileRepository(
    private val mainHttpClientManager: MainHttpClientManager,
): FileRepository {
    /**
     * Выполняет POST-запрос для создания нового файла или папки в указанном проекте.
     *
     * @param fileName Название создаваемого файла или папки.
     * @param projectId Идентификатор проекта, в котором создается элемент.
     * @param isFolder Флаг, указывающий, является ли элемент папкой (true) или файлом (false).
     * @param parentId Идентификатор родительской папки (null, если элемент создается в корне).
     * @return [Response] с результатом операции.
     */
    override suspend fun createFile(
        fileName: String,
        projectId: Int,
        isFolder: Boolean,
        parentId: Int?
    ): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().post(CREATE_FILE) {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateFileRequest(
                        fileName = fileName,
                        projectId = projectId,
                        isFolder = isFolder,
                        parentId = parentId
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    /**
     * Выполняет DELETE-запрос для удаления файла или папки из проекта по уникальному идентификатору.
     *
     * @param fileId Уникальный идентификатор удаляемого файла или папки.
     * @return [Response] с результатом операции.
     */
    override suspend fun deleteFile(fileId: Int): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().delete(DELETE_FILE) {
                contentType(ContentType.Application.Json)
                setBody(DeleteFileRequest(fileId = fileId))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    /**
     * Выполняет PUT-запрос для изменения имени существующего файла или папки.
     *
     * @param fileId Уникальный идентификатор переименовываемого элемента.
     * @param newName Новое имя для файла или папки.
     * @return [Response] с результатом операции.
     */
    override suspend fun renameFile(fileId: Int, newName: String): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().put(RENAME_FILE) {
                contentType(ContentType.Application.Json)
                setBody(RenameFileRequest(fileId, newName))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden ,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }
}