package ru.vsu.front.domain.repository

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
}