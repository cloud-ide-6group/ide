package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Response

/**
 * UseCase для обновления аватара пользователя.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class UpdateProfilePhotoUseCase(
    private val repository: ProfileRepository,
) {
    /**
     * Выполняет обновление аватара пользователя.
     *
     * @return [Response] с информацией об успехе запроса.
     */
    suspend operator fun invoke(
        photoBase64: String,
    ): Response<*> {
        return repository.updateProfilePhoto(
            photoBase64 = photoBase64
        )
    }
}
