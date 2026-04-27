package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

/**
 * UseCase для получения обновленных токенов.
 *
 * @property repository Интерфейс репозитория.
 */
class RefreshUseCase(
    private val repository: AuthRepository,
) {
    /**
     * Обновляет токены.
     *
     * @param refreshToken Токен обновления.
     * @return [Response] с access и refresh токенами при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        refreshToken: String
    ): Response<AuthTokens> {
        return repository.refresh(refreshToken = refreshToken)
    }
}