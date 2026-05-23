package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для приглашения пользователя в проект.
 *
 * @property repository Интерфейс репозитория.
 */
class InviteUserUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет приглашение пользователя в проект.
     *
     * @param userEmail Почта пользователя.
     * @param projectName Название проекта.
     *
     * @return [Response] с не важно чем (важен лишь код ответа), или с ошибкой.
     */
    suspend operator fun invoke(
        userEmail: String,
        projectName: String,
    ): Response<*> {
        return repository.inviteUser(userEmail = userEmail, projectName = projectName)
    }
}
