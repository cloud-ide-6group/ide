package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.ProjectInfo
import ru.vsu.front.model.entity.Response

/**
 * UseCase для получения информации о проекте.
 *
 * @property repository Интерфейс репозитория.
 */
class GetProjectInfoUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Получает информацию о проекте.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с информацией о проекте при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        projectId: Int,
    ): Response<ProjectInfo> {
        return repository.getProjectInfo(projectId)
    }
}