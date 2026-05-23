package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для подключения к комнате чата.
 *
 * @property repository Интерфейс репозитория.
 */
class JoinChatUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет присоединение к комнате чата.
     *
     * @param identificator Строковый идентификатор чата.
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(identificator: String, projectId: Int) {
        return repository.joinChatRoom(identificator, projectId)
    }
}
