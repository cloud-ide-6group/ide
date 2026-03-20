package ru.vsu.front.features.auth.domain.usecase

import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.repository.AuthRepository

/**
 * UseCase для регистрации пользователя.
 *
 * @property repository Интерфейс репозитория.
 */
class SignUseCase(
    private val repository: AuthRepository,
) {
    /**
     * Создаёт аккаунта.
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Не зашифрованный пароль пользователя.
     * @return [AuthResult] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): AuthResult<UserSession> {
        return repository.sign(name = name, email = email, password = password)
    }
}