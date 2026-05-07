package ru.vsu.front.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Response

/**
 * Интерфейс репозитория, связанного с уведомлениями.
 */
interface NotificationsRepository {
    /**
     * Выполняет подписку на получение уведомлений.

     * @return [Flow] со списком текущих уведомлений.
     */
    fun observeNotifications(): Flow<List<Notification>>

    /**
     * Выполняет удаление уведомления.

     * @return [Response] с не важно чем при успехе (главное код ответа), либо с ошибкой.
     */
    suspend fun deleteNotification(notificationId: Int): Response<*>
}