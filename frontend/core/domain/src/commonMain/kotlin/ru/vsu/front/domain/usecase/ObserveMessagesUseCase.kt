package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Message

/**
 * UseCase для подписки на обновления сообщений в выбранном чате.
 *
 * @property repository Интерфейс репозитория.
 */
class ObserveMessagesUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет подписку на получение списка сообщений для конкретного чата.
     *
     * @return [Flow] со списком сообщений чата.
     */
    operator fun invoke(): Flow<Pair<Int, List<Message>>> {
        return repository.observeMessages()
    }
}