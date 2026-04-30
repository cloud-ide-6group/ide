package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

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
     * @return [Response] с токенами при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Response<AuthTokens> {
        return repository.login(email = email, password = password)
    }
}
