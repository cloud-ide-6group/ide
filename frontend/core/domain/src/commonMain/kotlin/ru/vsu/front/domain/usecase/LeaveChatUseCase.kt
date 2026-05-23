package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для отключения от комнаты чата.
 *
 * @property repository Интерфейс репозитория.
 */
class LeaveChatUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет отключение от комнаты чата.
     *
     * @param chatId Идентификатор чата.
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(chatId: String, projectId: Int) {
        return repository.leaveChatRoom(chatId, projectId)
    }
}
