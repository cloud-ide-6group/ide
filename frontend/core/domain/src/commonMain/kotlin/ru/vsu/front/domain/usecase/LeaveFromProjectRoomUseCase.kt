package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для отключения от комнаты проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class LeaveFromProjectRoomUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет отключение от комнаты проекта.
     *
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(projectId: Int) {
        return repository.leaveFromProjectRoom(projectId)
    }
}
