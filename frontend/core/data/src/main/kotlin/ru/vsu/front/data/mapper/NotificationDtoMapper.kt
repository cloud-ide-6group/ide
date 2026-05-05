package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.NotificationDto
import ru.vsu.front.data.entity.dto.ProgramingLanguageDto
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Преобразует DTO-модель ([NotificationDto])
 * в доменную сущность языка программирования ([Notification]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [ProgramingLanguage], содержащая информацию о доступном языке.
 */
fun NotificationDto.toEntity(): Notification {
    return Notification(
        notificationId = notificationId,
        senderName = senderName,
        sendTime = sendTime,
        projectId = projectId,
        projectName = projectName
    )
}

/**
 * Преобразует список DTO-моделей ([NotificationDto])
 * в список доменных сущностей языков программирования ([Notification]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Список [Notification], содержащий информацию о доступных языках.
 */
fun List<NotificationDto>.toEntities(): List<Notification> {
    return map { it.toEntity() }
}