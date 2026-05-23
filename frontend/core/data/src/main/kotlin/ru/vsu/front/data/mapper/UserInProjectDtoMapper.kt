package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.UserDto
import ru.vsu.front.model.entity.User

/**
 * Преобразует DTO-модель ([UserDto])
 * в доменную сущность пользователя ([User]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [User], содержащая данные пользователя.
 */
fun UserDto.toEntity(): User {
    return User(
        userId = userId,
        name = name,
        email = email
    )
}

fun List<UserDto>.toEntities(): List<User> {
    return map { it.toEntity() }
}

