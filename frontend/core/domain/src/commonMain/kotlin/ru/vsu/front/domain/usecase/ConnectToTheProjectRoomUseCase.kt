package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для подключения к комнате проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class ConnectToTheProjectRoomUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет подключение к комнате проекта через веб-сокет.
     *
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(projectId: Int) {
        return repository.connectToTheProjectRoom(projectId)
    }
}
