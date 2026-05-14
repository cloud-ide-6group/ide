package ru.vsu.front.data.entity.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO уведомления.
 *
 * @property notificationId Идентификатор уведомления.
 * @property sendTime Время отправки.
 * @property senderName Имя отправителя.
 * @property projectId Идентификатор проекта, в который был приглашён пользователь.
 * @property projectName Название проекта, в который был приглашён пользователь.
 */
@Serializable
data class NotificationDto(
    @SerialName("notification_id") val notificationId: Int,
    @SerialName("send_time") val sendTime: String,
    @SerialName("sender_name") val senderName: String,
    @SerialName("project_id") val projectId: Int,
    @SerialName("project_name") val projectName: String,
)
