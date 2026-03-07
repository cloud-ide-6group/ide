@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase

/**
 * Вьюмодель экрана логина
 *
 * @param loginUseCase Юзкейс авторизации
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState.asStateFlow()

    fun processCommand(command: LoginCommand) {
        when(val command = command) {
            is LoginCommand.ChangeEmail -> {
                _uiState.update { previousState ->
                    previousState.copy(email = command.email)
                }
            }

            is LoginCommand.ChangePassword -> {
                _uiState.update { previousState ->
                    previousState.copy(password = command.password)
                }
            }

            LoginCommand.ClickLogin -> {
                viewModelScope.launch(Dispatchers.IO) {
                    loginUseCase(
                        email = _uiState.value.email,
                        password = _uiState.value.password
                    )
                }
            }

            LoginCommand.ChangePasswordVisibility -> {
                _uiState.update { previousState ->
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
data class UiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)