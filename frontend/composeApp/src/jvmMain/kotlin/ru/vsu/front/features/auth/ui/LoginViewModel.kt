@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase

/**
 * Вьюмодель экрана логина
 *
 * @param loginUseCase Юзкейс авторизации
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _uiStateLogin = MutableStateFlow<UiStateLogin>(UiStateLogin())
    val uiStateLogin: StateFlow<UiStateLogin>
        get() = _uiStateLogin.asStateFlow()

    private val _events = MutableSharedFlow<LoginEffect>()
    val events: SharedFlow<LoginEffect>
        get() = _events.asSharedFlow()

    fun processCommand(command: LoginCommand) {
        when(val command = command) {
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
                viewModelScope.launch(Dispatchers.IO) {
                    val result = loginUseCase(
                        email = _uiStateLogin.value.email,
                        password = _uiStateLogin.value.password
                    )
                    when(result) {
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
 * Комманды для вьюмодели логина
 *
 * @see ChangeEmail Сменить почту
 * @see ChangePassword Сменить пароль
 * @see ChangePasswordVisibility Сменить видимость пароля
 * @see ClickLogin Нажатие на кнопку Login
 */
sealed interface LoginCommand {
    data class ChangeEmail(val email: String) : LoginCommand
    data class ChangePassword(val password: String) : LoginCommand
    data object ChangePasswordVisibility : LoginCommand
    data object ClickLogin : LoginCommand
}

/**
 * Состояние экрана логина
 *
 * @see email Текущая почта
 * @see password текущий пароль
 * @see isPasswordVisible Видимость пароля
 */
data class UiStateLogin(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)

sealed interface LoginEffect {
    data class ShowError(val message: String) : LoginEffect
}