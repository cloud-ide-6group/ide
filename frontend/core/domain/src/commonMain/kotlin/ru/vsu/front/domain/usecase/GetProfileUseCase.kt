package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User

/**
 * UseCase для получения профиля пользователя.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class GetProfileUseCase(
    private val repository: ProfileRepository,
) {
    /**
     * Выполняет вход в систему.
     *
     * @param userId Айди пользователя.
     *
     * @return [Response] с профилем пользователя при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        userId: Int,
    ): Response<User> {
        return repository.getProfile(userId = userId)
    }
}
