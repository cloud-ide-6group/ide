package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ChatRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для создания нового чат.
 *
 * @property repository Интерфейс репозитория.
 */
class CreateChatUseCase(
    private val repository: ChatRepository,
) {
    /**
     * Выполняет создание чата в проекте.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с информацией об успехе запроса или ошибке.
     */
    suspend operator fun invoke(
        projectId: Int,
        identificator: String
    ): Response<*> {
        return repository.createChat(projectId, identificator)
    }
}
