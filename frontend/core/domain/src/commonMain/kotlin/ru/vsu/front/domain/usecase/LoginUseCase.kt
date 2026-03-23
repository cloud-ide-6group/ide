package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserData

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
     * @return [Response] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Response<UserData> {
        return repository.login(email = email, password = password)
    }
}
