package ru.vsu.front.data.mapper

import ru.vsu.front.data.dto.UserDto
import ru.vsu.front.model.entity.User

/**
 * Преобразует DTO-модель ([UserDto])
 * в доменную сущность пользователя ([User]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 * * @return Модель [User], содержащая данные пользователя.
 */
fun UserDto.toEntity(userId: Int): User {
    return User(
        id = userId,
        name = name,
        email = email,
        photo = photo,
        projects = emptyList()
    )
}