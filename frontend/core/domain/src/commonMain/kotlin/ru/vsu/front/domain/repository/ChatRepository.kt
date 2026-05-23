package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response

/**
 * Интерфейс репозитория для работы с чатом.
 */
interface ChatRepository {

    /**
     * Выполняет создание чата.

     * @return [Response] с не важно чем при успехе (главное код ответа), либо с ошибкой.
     */
    suspend fun createChat(
        projectId: Int,
        identificator: String
    ): Response<*>


    /**
     * Выполняет создание сообщения.

     * @return [Response] с не важно чем при успехе (главное код ответа), либо с ошибкой.
     */
    suspend fun createMessage(
        chatId: Int,
        text: String
    ): Response<*>
}