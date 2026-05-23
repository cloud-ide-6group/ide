package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для подписки на событие исключения пользователя из проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class ObserveRemovedFromProjectUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет подписку на событие исключения пользователя из проекта.
     *
     * @return [Flow] с идентификатором проекта, из которого был исключен пользователь.
     */
    operator fun invoke(): Flow<Int> {
        return repository.observeRemovedFromProject()
    }
}
