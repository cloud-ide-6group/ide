package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для обновления пароля пользователя.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class UpdateProfilePasswordUseCase(
    private val repository: ProfileRepository,
) {
    /**
     * Выполняет обновление пароля пользователя.
     *
     * @return [Response] с информацией об успехе запроса.
     */
    suspend operator fun invoke(
        oldPassword: String,
        newPassword: String,
    ): Response<*> {
        return repository.updateProfilePassword(
            oldPassword = oldPassword,
            newPassword = newPassword
        )
    }
}
