package ru.vsu.front.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.model.entity.Response

/**
 * Интерфейс репозитория для работы с платными функциями.
 */
interface PremiumRepository {

    /**
     * Выполняет оформление подписки.
     *
     * @return [Response] с идентификатором проекта, либо с ошибкой.
     */
    suspend fun subscribe(): Response<*>
    /**
     * Выполняет подписку на конец подписки (премиум).
     *
     * @return [Flow] с информацией, закончена ли подписка.
     */
    fun observeSubscriptionExpired(): Flow<Boolean>
}