package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос создания сообщения.
 *
 * @property chatId Идентификатор чата.
 * @property text Текст сообщения.
 */
@Serializable
class CreateMessageRequest(
    @SerialName("chat_id") val chatId: Int,
    @SerialName("message_text") val text: String
) {
}