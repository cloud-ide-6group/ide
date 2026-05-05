package ru.vsu.front.model.entity

/**
 * Уведомление (о приглашении в проект).
 *
 * @property notificationId Идентификатор уведомления.
 * @property senderName Имя отправителя.
 * @property sendTime Время отправки.
 * @property projectId Идентификатор проекта, в который приглашён пользователь.
 * @property projectName Название проекта, в который приглашён пользователь.
 */
data class Notification(
    val notificationId: Int,
    val senderName: String,
    val sendTime: String,
    val projectId: Int,
    val projectName: String,
)
