package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.FileRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для создания нового файла или папки.
 *
 * @property repository Интерфейс репозитория.
 */
class CreateFileUseCase(
    private val repository: FileRepository,
) {
    /**
     * Выполняет создание файла или папки в проекте.
     *
     * @param fileName Название создаваемого файла или папки.
     * @param projectId Идентификатор проекта.
     * @param isFolder Флаг, указывающий, является ли элемент папкой.
     * @param parentId Идентификатор родительской папки (null, если в корне).
     *
     * @return [Response] с информацией об успехе запроса или ошибке.
     */
    suspend operator fun invoke(
        fileName: String,
        projectId: Int,
        isFolder: Boolean,
        parentId: Int?
    ): Response<*> {
        return repository.createFile(
            fileName = fileName,
            projectId = projectId,
            isFolder = isFolder,
            parentId = parentId
        )
    }
}
