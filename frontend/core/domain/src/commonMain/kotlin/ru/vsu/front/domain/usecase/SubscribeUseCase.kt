package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.PremiumRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для оформления подписки.
 *
 * @property repository Интерфейс репозитория.
 */
class SubscribeUseCase(
    private val repository: PremiumRepository,
) {
    /**
     * Выполняет оформление подписки.
     *
     * @return [Response] с информацией об успехе запроса или ошибке.
     */
    suspend operator fun invoke(): Response<*> {
        return repository.subscribe()
    }
}
