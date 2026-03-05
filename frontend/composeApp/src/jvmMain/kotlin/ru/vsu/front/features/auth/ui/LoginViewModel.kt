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
import ru.vsu.front.features.auth.domain.usecase.SignUseCase

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
        }
    }
}

sealed interface LoginCommand {
    data class ChangeEmail(val email: String) : LoginCommand
    data class ChangePassword(val password: String) : LoginCommand
    data object ClickLogin : LoginCommand
}

data class UiState(
    val email: String = "",
    val password: String = ""
)