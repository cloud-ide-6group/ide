package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

/**
 * UseCase для обновления токенов.
 *
 * @property repository Интерфейс репозитория.
 */
class RefreshUseCase(
    private val repository: AuthRepository,
) {
    /**
     * Создаёт аккаунта.
     *
     * @param accessToken Токен доступа.
     * @param refreshToken Токен обновления.
     * @return [Response] с access и refresh токенами при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        accessToken: String,
        refreshToken: String
    ): Response<AuthTokens> {
        return repository.refresh(accessToken = accessToken, refreshToken = refreshToken)
    }
}