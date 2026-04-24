package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserProfile

/**
 * Интерфейс репозитория для работы с профилем.
 */
interface ProfileRepository {

    /**
     * Выполняет получение профиля.

     * @return [Response] с информацией о профиле пользователя, либо с ошибкой.
     */
    suspend fun getProfile(): Response<UserProfile>

    /**
     * Выполняет получение профиля.

     * @return [Response] с информацией об успехе запроса.
     */
    suspend fun updateProfileData(email: String, name: String): Response<*>

    /**
     * Выполняет получение профиля.

     * @return [Response] с информацией об успехе запроса.
     */
    suspend fun updateProfilePassword(
        newPassword: String,
        oldPassword: String,
    ): Response<*>

    /**
     * Выполняет получение профиля.

     * @return [Response] с информацией об успехе запроса.
     */
    suspend fun updateProfilePhoto(photoBase64: String): Response<*>
}