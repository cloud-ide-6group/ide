package ru.vsu.front.data.mapper

import ru.vsu.front.data.dto.UserDataDto
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.User
import ru.vsu.front.model.entity.UserData

/**
 * Преобразует DTO-модель ([UserDataDto])
 * в доменную сущность пользователя ([UserData]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 * * @return Модель [UserData], содержащая данные пользователя и токены.
 */
fun UserDataDto.toEntity(): UserData {
    return UserData(
        user = User(
            name = name,
            email = email,
            photoPath = photoPath,
            projects = emptyList()
        ),
        tokens = AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    )
}