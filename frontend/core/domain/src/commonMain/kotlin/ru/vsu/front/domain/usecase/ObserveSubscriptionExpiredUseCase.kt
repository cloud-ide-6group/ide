package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.PremiumRepository

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
