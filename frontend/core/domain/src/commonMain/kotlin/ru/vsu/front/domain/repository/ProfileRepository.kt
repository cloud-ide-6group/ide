package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User

/**
 * Интерфейс репозитория для аутентификации и регистрации.
 */
interface ProfileRepository {

    /**
     * Выполняет получение профиля.

     * @return [Response] с информацией о профиле пользователя, либо с ошибкой.
     */
    suspend fun getProfile(userId: Int): Response<User>
}