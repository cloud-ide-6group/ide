package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос на удаление уведомления.
 *
 * @property notificationId Идентификатор уведомления.
 */
@Serializable
data class DeleteNotificationRequest(
    @SerialName("notification_id") val notificationId: Int
)
