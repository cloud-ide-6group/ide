package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для запуска выполнения кода проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class RunCodeUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет запуск кода проекта.
     *
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(projectId: Int) {
        return repository.runCode(projectId)
    }
}
