@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.domain.usecase

import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.repository.AuthRepository

/**
 * Юзкейс регистрации
 *
 * @param repository Репозиторий с реализацией логина, регистрации
 */
class SignUseCase(
    private val repository: AuthRepository,
) {
    /**
     * @param name Имя пользователя
     * @param email Почта пользователя
     * @param password Пароль пользователя
     */
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): AuthResult<UserSession> {
        return repository.sign(name = name, email = email, password = password)
    }
}