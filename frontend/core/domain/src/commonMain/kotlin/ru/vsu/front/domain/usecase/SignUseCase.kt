package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.AuthRepository
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserData

/**
 * UseCase для регистрации пользователя.
 *
 * @property repository Интерфейс репозитория.
 */
class SignUseCase(
    private val repository: AuthRepository,
) {
    /**
     * Создаёт аккаунт.
     *
     * @param name Имя пользователя.
     * @param email Почта пользователя.
     * @param password Не зашифрованный пароль пользователя.
     * @return [Response] с сессией пользователя при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Response<UserData> {
        return repository.sign(name = name, email = email, password = password)
    }
}