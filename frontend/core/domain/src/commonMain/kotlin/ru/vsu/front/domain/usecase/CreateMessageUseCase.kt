package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ChatRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для создания (отправки) нового сообщения в чат.
 *
 * @property repository Интерфейс репозитория для работы с чатами.
 */
class CreateMessageUseCase(
    private val repository: ChatRepository,
) {
    /**
     * Выполняет создание нового сообщения.
     *
     * @param chatId Идентификатор чата.
     * @param text Текст отправляемого сообщения.
     *
     * @return [Response] с результатом выполнения запроса.
     */
    suspend operator fun invoke(
        chatId: Int,
        text: String
    ): Response<*> {
        return repository.createMessage(chatId, text)
    }
}
