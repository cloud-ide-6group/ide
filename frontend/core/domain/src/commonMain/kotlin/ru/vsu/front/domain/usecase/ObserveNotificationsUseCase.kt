package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.Response

/**
 * UseCase для подписки на получение уведомлений.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class ObserveNotificationsUseCase(
    private val repository: NotificationsRepository,
) {
    /**
     * Выполняет подписку на получение уведомлений.
     *
     * @return [Flow] со списком текущих уведомлений.
     */
    operator fun invoke(): Flow<List<Notification>> {
        return repository.observeNotifications()
    }
}
