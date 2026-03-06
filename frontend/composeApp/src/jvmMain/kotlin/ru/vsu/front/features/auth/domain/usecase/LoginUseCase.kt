@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.domain.usecase

import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.repository.AuthRepository

/**
 * Юзкейс авторизации
 *
 * @param repository Репозиторий с реализацией логина, регистрации
 */
class LoginUseCase(
    private val repository: AuthRepository,
) {
    /**
     * @param email Почта пользователя
     * @param password Пароль пользователя
     */
    suspend operator fun invoke(email: String, password: String): AuthResult<UserSession> {
        return repository.login(email = email, password = password)
    }
}
