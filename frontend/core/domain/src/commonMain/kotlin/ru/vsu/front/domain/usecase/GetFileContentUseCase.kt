package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для получения содержимого определенного файла.
 *
 * @property repository Интерфейс репозитория.
 */
class GetFileContentUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Отправляет запрос на получение содержимого определенного файла.
     *
     * @param fileId Идентификатор файла.
     */
    suspend operator fun invoke(fileId: Int) {
        repository.getFileContent(fileId)
    }
}
