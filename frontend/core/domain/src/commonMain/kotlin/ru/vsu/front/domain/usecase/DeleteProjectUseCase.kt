package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для удаления проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class DeleteProjectUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Удаляет проект.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с не важно чем (важен только код ответа), либо с ошибкой.
     */
    suspend operator fun invoke(
        projectId: Int,
    ): Response<*> {
        return repository.deleteProject(projectId)
    }
}