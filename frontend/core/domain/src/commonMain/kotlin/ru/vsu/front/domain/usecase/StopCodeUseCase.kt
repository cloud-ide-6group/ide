package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для остановки выполнения кода проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class StopCodeUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет остановку запущенного кода.
     *
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(projectId: Int) {
        return repository.stopCode(projectId)
    }
}
