package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.PremiumRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.ConsoleOutput

/**
 * UseCase для подписки на вывод консоли запущенного проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class ObserveSubscriptionExpiredUseCase(
    private val repository: PremiumRepository,
) {
    /**
     * Выполняет подписку на получение потока вывода консоли.
     *
     * @return [Flow] с информацией, закончена ли подписка.
     */
    operator fun invoke(): Flow<Boolean> {
        return repository.observeSubscriptionExpired()
    }
}
