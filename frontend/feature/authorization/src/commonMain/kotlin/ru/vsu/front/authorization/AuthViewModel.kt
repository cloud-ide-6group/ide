package ru.vsu.front.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.LoginUseCase
import ru.vsu.front.domain.usecase.SignUseCase
import ru.vsu.front.domain.validation.EmailMatcher
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.Response

/**
 * Вьюмодель экрана авторизации и регистрации.
 *
 * @param loginUseCase UseCase для авторизации.
 * @param signUseCase UseCase для регистрации.
 * @param tokenStorage Хранилище JWT-токенов.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
 */
class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val signUseCase: SignUseCase,
    private val tokenStorage: TokenStorage,
    private val dispatcherProvider: DispatcherProvider,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiStateAuth())
    val uiState: StateFlow<UiStateAuth>
        get() = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEffect>()
    val events: SharedFlow<AuthEffect>
        get() = _events.asSharedFlow()

    /**
     * Выполняет определенные действия в зависимости от переданной команды.
     */
    fun processCommand(command: AuthCommand) {
        when (command) {
            is AuthCommand.ChangeName -> {
                _uiState.update { it.copy(name = command.name) }
            }

            is AuthCommand.ChangeEmail -> {
                _uiState.update { it.copy(email = command.email) }
            }

            is AuthCommand.ChangePassword -> {
                _uiState.update { it.copy(password = command.password) }
            }

            is AuthCommand.ChangeConfirmedPassword -> {
                _uiState.update { it.copy(confirmedPassword = command.confirmedPassword) }
            }

            AuthCommand.ChangePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }

            AuthCommand.ChangeConfirmedPasswordVisibility -> {
                _uiState.update { it.copy(isConfirmedPasswordVisible = !it.isConfirmedPasswordVisible) }
            }

            AuthCommand.ClickLogin -> login()
            AuthCommand.ClickSignUp -> signUp()
        }
    }

    private fun login() {
        viewModelScope.launch(dispatcherProvider.io) {
            val state = _uiState.value
            if (!EmailMatcher.isValid(state.email)) {
                _events.emit(AuthEffect.ShowError("Недопустимая почта"))
                return@launch
            }

            _uiState.update { it.copy(buttonEnabled = false) }

            val result = loginUseCase(
                email = state.email,
                password = state.password
            )

            handleAuthResult(result)
        }
    }

    private fun signUp() {
        viewModelScope.launch(dispatcherProvider.io) {
            val state = _uiState.value

            if (!EmailMatcher.isValid(state.email)) {
                _events.emit(AuthEffect.ShowError("Недопустимая почта"))
                return@launch
            }

            if (state.password != state.confirmedPassword) {
                _events.emit(AuthEffect.ShowError("Пароли не совпадают"))
                return@launch
            }

            _uiState.update { it.copy(buttonEnabled = false) }

            val result = signUseCase(
                name = state.name,
                email = state.email,
                password = state.password
            )

            handleAuthResult(result)
        }
    }

    private suspend fun handleAuthResult(result: Response<AuthTokens>) {
        when (result) {
            is Response.Error<*> -> {
                _events.emit(AuthEffect.ShowError(result.requestError.message))
                _uiState.update { it.copy(buttonEnabled = true) }
            }

            is Response.Success<AuthTokens> -> {
                val tokens = result.data

                tokenStorage.saveToken(token = tokens.accessToken, isAccess = true)
                tokenStorage.saveToken(token = tokens.refreshToken, isAccess = false)

                val userId = tokenStorage.getUserIdFromToken()

                userId?.let {
                    authManager.onLoginSuccess(it)
                }
            }
        }
    }
}

/**
 * Команды.
 */
sealed interface AuthCommand {
    data class ChangeName(val name: String) : AuthCommand
    data class ChangeEmail(val email: String) : AuthCommand
    data class ChangePassword(val password: String) : AuthCommand
    data class ChangeConfirmedPassword(val confirmedPassword: String) : AuthCommand
    data object ChangePasswordVisibility : AuthCommand
    data object ChangeConfirmedPasswordVisibility : AuthCommand
    data object ClickLogin : AuthCommand
    data object ClickSignUp : AuthCommand
}

/**
 * Состояние экрана.
 */
data class UiStateAuth(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmedPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmedPasswordVisible: Boolean = false,
    val buttonEnabled: Boolean = true
)

/**
 * События экрана аутентификации.
 */
sealed interface AuthEffect {
    data class ShowError(val message: String) : AuthEffect
}