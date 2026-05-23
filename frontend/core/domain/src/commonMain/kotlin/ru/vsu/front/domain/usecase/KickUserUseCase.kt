package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для исключения участника из проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class KickUserUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет исключение участника из проекта.
     *
     * @param userEmail Почта участника, который будет исключен.
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с информацией об успехе запроса или ошибке.
     */
    suspend operator fun invoke(
        userEmail: String,
        projectId: Int,
    ): Response<*> {
        return repository.kickUser(userEmail, projectId)
    }
}
