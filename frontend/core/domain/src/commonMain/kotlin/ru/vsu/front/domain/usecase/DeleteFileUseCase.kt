package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.FileRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для удаления файла или папки.
 *
 * @property repository Интерфейс репозитория.
 */
class DeleteFileUseCase(
    private val repository: FileRepository,
) {
    /**
     * Выполняет удаление файла или папки по идентификатору.
     *
     * @param fileId Идентификатор удаляемого файла.
     * @return [Response] с информацией об успехе запроса или ошибке.
     */
    suspend operator fun invoke(
        fileId: Int,
    ): Response<*> {
        return repository.deleteFile(fileId = fileId,)
    }
}
