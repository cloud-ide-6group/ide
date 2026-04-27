package ru.vsu.front.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.vsu.front.datastore.TokenStorage

/**
 * Менеджер аутентификации.
 *
 * @property tokenStorage хранилище токенов.
 */
class AuthManager(private val tokenStorage: TokenStorage) {
    /**
     * Текущее состояние авторизации.
     */
    private val _isAuthorized = MutableStateFlow<AuthState>(checkAuthorized())
    val isAuthorized = _isAuthorized.asStateFlow()

    /**
     * Проверяет текущее состояние авторизации.
     *
     * @return [AuthState.Authorized], если токен валиден, иначе [AuthState.NotAuthorized].
     */
    private fun checkAuthorized(): AuthState {
        val userId = tokenStorage.getUserIdFromToken()
        return if (userId != null) AuthState.Authorized(userId) else AuthState.NotAuthorized
    }

    /**
     * Обрабатывает успешный вход пользователя в систему.
     */
    fun onLoginSuccess(userId: Int) {
        _isAuthorized.value = AuthState.Authorized(userId)
    }

    /**
     * Выполняет выход пользователя из аккаунта.
     */
    fun logout() {
        tokenStorage.clearTokens()
        _isAuthorized.value = AuthState.NotAuthorized
    }
}

/**
 * Состояние авторизации пользователя в приложении.
 */
sealed interface AuthState {

    /**
     * Пользователь авторизован.
     * @property userId Идентификатор текущего пользователя.
     */
    data class Authorized(val userId: Int) : AuthState

    /**
     * Пользователь не авторизован.
     */
    data object NotAuthorized : AuthState
}