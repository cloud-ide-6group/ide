package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для удаления уведомления.
 *
 * @property repository Интерфейс репозитория.
 *
 */
data class DeleteNotificationUseCase(
    private val repository: NotificationsRepository
) {
    /**
     * Выполняет создания проекта.
     *
     * @param notificationId Идентификатор уведомления.
     *
     * @return [Response] с ничем, либо с ошибкой.
     */
    suspend operator fun invoke(
        notificationId: Int,
    ): Response<*> {
        return repository.deleteNotification(notificationId)
    }
}
