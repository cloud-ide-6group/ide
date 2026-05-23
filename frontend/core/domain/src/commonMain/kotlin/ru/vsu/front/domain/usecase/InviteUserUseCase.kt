package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User

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
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с не важно чем (важен лишь код ответа), или с ошибкой.
     */
    suspend operator fun invoke(
        userEmail: String,
        projectId: Int
    ): Response<User> {
        return repository.inviteUser(userEmail = userEmail, projectId = projectId)
    }
}
