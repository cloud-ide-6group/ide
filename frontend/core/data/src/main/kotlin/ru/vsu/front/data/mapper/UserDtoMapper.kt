package ru.vsu.front.data.mapper

import ru.vsu.front.data.dto.UserProfileDto
import ru.vsu.front.model.entity.UserProfile

/**
 * Преобразует DTO-модель ([UserProfileDto])
 * в доменную сущность пользователя ([UserProfile]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 * * @return Модель [UserProfile], содержащая данные пользователя.
 */
fun UserProfileDto.toEntity(userId: Int): UserProfile {
    return UserProfile(
        id = userId,
        name = name,
        email = email,
        photo = photo,
        projects = projects.toEntities()
    )
}