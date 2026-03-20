package ru.vsu.front.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.di.dispatcher_provider.DispatcherProvider
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase

/**
 * Вьюмодель экрана авторизации.
 *
 * @param loginUseCase UseCase для запроса авторизации.
 * @param tokenStorage Локальное хранилище для JWT-токенов.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenStorage: TokenStorage,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiStateLogin = MutableStateFlow(UiStateLogin())
    val uiStateLogin: StateFlow<UiStateLogin>
        get() = _uiStateLogin.asStateFlow()

    private val _events = MutableSharedFlow<LoginEffect>()
    val events: SharedFlow<LoginEffect>
        get() = _events.asSharedFlow()

    fun processCommand(command: LoginCommand) {
        when (val command = command) {
            is LoginCommand.ChangeEmail -> {
                _uiStateLogin.update { previousState ->
                    previousState.copy(email = command.email)
                }
            }

            is LoginCommand.ChangePassword -> {
                _uiStateLogin.update { previousState ->
                    previousState.copy(password = command.password)
                }
            }

            LoginCommand.ClickLogin -> {
                viewModelScope.launch(dispatcherProvider.default) {
                    val result = loginUseCase(
                        email = _uiStateLogin.value.email,
                        password = _uiStateLogin.value.password
                    )
                    when (result) {
                        is AuthResult.Error<*> -> {
                            _events.emit(LoginEffect.ShowError(result.errorData.message))
                        }

                        is AuthResult.Success<UserSession> -> {
                            val tokens = result.data.tokens
                            tokenStorage.saveToken(token = tokens.accessToken, isAccess = true)
                            tokenStorage.saveToken(token = tokens.refreshToken, isAccess = false)
                        }
                    }
                }
            }

            LoginCommand.ChangePasswordVisibility -> {
                _uiStateLogin.update { previousState ->
                    previousState.copy(isPasswordVisible = !previousState.isPasswordVisible)
                }
            }
        }
    }
}

/**
 * Набор команд.
 * Описывает все возможные действия пользователя на экране.
 */
sealed interface LoginCommand {
    /**
     * Команда обновления введенного текста в поле "Почта".
     * */
    data class ChangeEmail(val email: String) : LoginCommand

    /**
     * Команда обновления введенного текста в поле "Пароль".
     * */
    data class ChangePassword(val password: String) : LoginCommand

    /**
     * Команда переключения видимости текста пароля.
     * */
    data object ChangePasswordVisibility : LoginCommand

    /**
     * Команда попытки входа.
     * */
    data object ClickLogin : LoginCommand
}

/**
 * Состояние экрана авторизации.
 *
 * @property email Текущий введенный текст в поле почты.
 * @property password Текущий введенный текст в поле пароля.
 * @property isPasswordVisible Видимость пароля.
 */
data class UiStateLogin(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)

/**
 * События экрана авторизации.
 */
sealed interface LoginEffect {
    /**
     * Событие показа уведомления с ошибкой.
     * @property message Сообщение ошибки.
     */
    data class ShowError(val message: String) : LoginEffect
}