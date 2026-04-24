package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для создания проекта.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class CreateProjectUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет создания проекта.
     *
     * @param programingLanguageId Айди языка программирования.
     * @param projectName Название проекта.
     *
     * @return [Response] с ничем, либо с ошибкой.
     */
    suspend operator fun invoke(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int> {
        return repository.createProject(
            programingLanguageId,
            projectName
        )
    }
}
