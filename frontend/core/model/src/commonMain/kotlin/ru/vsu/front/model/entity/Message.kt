package ru.vsu.front.model.entity

/**
 * Сообщение.
 *
 * @property id Идентификатор сообщения.
 * @property text Текст сообщения.
 * @property author Автор сообщения.
 * @property sendTime Время отправки сообщения.
 */
data class Message(
    val id: Int,
    val text: String,
    val author: String,
    val sendTime: String,
)
