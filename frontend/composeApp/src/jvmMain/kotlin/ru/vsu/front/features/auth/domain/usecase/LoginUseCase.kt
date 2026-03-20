package ru.vsu.front.features.auth.domain.usecase

import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.repository.AuthRepository

/**
 * UseCase для авторизации пользователя.
 *
 * @property repository Интерфейс репозитория для выполнения запроса.
 */
class LoginUseCase(
    private val repository: AuthRepository,
) {
    /**
     * Выполняет вход в систему.
     *
     * @param email Почта пользователя.
     * @param password Не зашифрованный пароль пользователя.
     * @return [AuthResult] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(email: String, password: String): AuthResult<UserSession> {
        return repository.login(email = email, password = password)
    }
}
