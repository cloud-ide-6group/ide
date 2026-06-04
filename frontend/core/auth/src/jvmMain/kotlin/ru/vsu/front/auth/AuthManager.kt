package ru.vsu.front.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.vsu.front.datastore.token_storage.TokenStorage
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.domain.socket.SocketHandler

/**
 * Менеджер аутентификации.
 *
 * @property tokenStorage хранилище токенов.
 */
class AuthManager(
    private val tokenStorage: TokenStorage,
    private val socketHandler: SocketHandler,
) : KoinComponent {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Интерфейс репозитория проекта, чтобы закрыть подключение к проекту при выходе из аккаунта.
     */
    private val projectRepository: ProjectRepository by inject()

    /**
     * Авторизован ли пользователь.
     */
    private val _isAuthorized = MutableStateFlow<AuthState>(checkAuthorized())
    val isAuthorized = _isAuthorized.asStateFlow()

    /**
     * Проверяет, авторизован ли пользователь.
     *
     * @return [AuthState.Authorized], если авторизован, [AuthState.NotAuthorized] если не авторизован.
     */
    private fun checkAuthorized(): AuthState {
        val userId = tokenStorage.getUserIdFromToken()
        return if (userId != null) AuthState.Authorized(userId) else AuthState.NotAuthorized
    }

    /**
     * Выполняется при успешном входе в аккаунт.
     *
     * @param userId Идентификатор пользователя.
     */
    fun onLoginSuccess(userId: Int) {
        _isAuthorized.value = AuthState.Authorized(userId)
    }


    /**
     * Выполняется при выходе из аккаунта.
     *
     * Очищает токены, закрывает подключение к проекту, меняете состояние на "Не авторизован".
     */
    fun logout() {
        socketHandler.closeSocket()
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
     *
     * @property userId Идентификатор текущего пользователя.
     */
    data class Authorized(val userId: Int) : AuthState

    /**
     * Пользователь не авторизован.
     */
    data object NotAuthorized : AuthState
}