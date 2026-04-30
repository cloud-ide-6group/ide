package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserProfile

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
     * Выполняет получение профиля.
     *
     * @return [Response] с профилем пользователя при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
    ): Response<UserProfile> {
        return repository.getProfile()
    }
}
