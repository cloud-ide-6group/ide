package ru.vsu.front.features.auth.data.mapper

import ru.vsu.front.features.auth.data.dto.AuthResponseDto
import ru.vsu.front.features.auth.domain.entity.AuthTokens
import ru.vsu.front.features.auth.domain.entity.User
import ru.vsu.front.features.auth.domain.entity.UserSession

/**
 * Преобразует DTO-модель ([AuthResponseDto])
 * в доменную сущность пользователя ([UserSession]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 * * @return Модель [UserSession], содержащая данные пользователя и токены.
 */
fun AuthResponseDto.toEntity(): UserSession {
    return UserSession(
        user = User(
            name = name,
            email = email,
            photoPath = photoPath
        ),
        tokens = AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    )
}