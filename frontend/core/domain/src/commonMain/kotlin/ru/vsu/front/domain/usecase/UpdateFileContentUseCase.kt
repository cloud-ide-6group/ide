package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для обновления содержимого файла.
 *
 * @property repository Интерфейс репозитория.
 */
class UpdateFileContentUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет отправку новых данных файла (текста).
     *
     * @param fileId Идентификатор файла.
     * @param content Новое содержимое файла.
     */
    suspend operator fun invoke(fileId: Int, content: String) {
        repository.updateFileContent(fileId, content)
    }
}