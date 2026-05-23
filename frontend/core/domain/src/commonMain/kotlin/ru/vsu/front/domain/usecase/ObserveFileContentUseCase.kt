package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для подписки на получение содержимого файла.
 *
 * @property repository Интерфейс репозитория.
 */
class ObserveFileContentUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет подписку на актуальное текстовое содержимое файла.
     *
     * @param fileId Идентификатор файла.
     *
     * @return [Flow] со строкой, содержащей код (текст) файла.
     */
    operator fun invoke(fileId: Int): Flow<String> {
        return repository.observeFileContent(fileId)
    }
}
