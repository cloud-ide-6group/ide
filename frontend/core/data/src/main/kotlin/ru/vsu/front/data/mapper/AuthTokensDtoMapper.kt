package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.AuthTokensDto
import ru.vsu.front.model.entity.AuthTokens

/**
 * Преобразует DTO-модель ([AuthTokensDto])
 * в доменную сущность пользователя ([AuthTokens]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [AuthTokens], содержащая токены.
 */
fun AuthTokensDto.toEntity(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}