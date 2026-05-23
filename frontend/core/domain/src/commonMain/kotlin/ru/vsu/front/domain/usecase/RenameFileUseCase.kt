package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.FileRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для переименования файла или папки.
 *
 * @property repository Интерфейс репозитория.
 */
class RenameFileUseCase(
    private val repository: FileRepository,
) {
    /**
     * Выполняет переименование указанного файла или папки.
     *
     * @param fileId Идентификатор файла или папки.
     * @param newName Новое имя элемента.
     *
     * @return [Response] с информацией об успехе запроса или ошибке.
     */
    suspend operator fun invoke(
        fileId: Int,
        newName: String
    ): Response<*> {
        return repository.renameFile(fileId, newName)
    }
}
