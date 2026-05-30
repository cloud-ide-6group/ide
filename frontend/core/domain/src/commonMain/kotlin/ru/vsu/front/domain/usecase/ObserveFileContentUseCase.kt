package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.FileContent

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
     * @return [Flow] со строкой, содержащей код (текст) файла.
     */
    operator fun invoke(): Flow<FileContent> {
        return repository.observeFileContent()
    }
}
