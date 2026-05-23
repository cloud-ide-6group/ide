package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для подписки на вывод консоли запущенного проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class ObserveConsoleOutputUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет подписку на получение потока вывода консоли.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Flow] со строками вывода консоли.
     */
    operator fun invoke(projectId: Int): Flow<String> {
        return repository.observeConsoleOutput(projectId)
    }
}
