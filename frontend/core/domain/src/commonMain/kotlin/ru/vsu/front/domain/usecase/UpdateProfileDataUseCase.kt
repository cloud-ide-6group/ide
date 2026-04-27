package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для обновления почты и логина пользователя.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class UpdateProfileDataUseCase(
    private val repository: ProfileRepository,
) {
    /**
     * Выполняет обновление почты и логина пользователя.
     *
     * @return [Response] с информацией об успехе запроса.
     */
    suspend operator fun invoke(
        email: String,
        name: String,
    ): Response<*> {
        return repository.updateProfileData(
            email = email,
            name = name
        )
    }
}
