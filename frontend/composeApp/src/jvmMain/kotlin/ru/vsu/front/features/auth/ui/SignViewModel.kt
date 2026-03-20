package ru.vsu.front.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.di.dispatcher_provider.DispatcherProvider
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.usecase.SignUseCase
import ru.vsu.front.features.auth.ui.SignEffect.ShowError

/**
 * Вьюмодель экрана регистрации
 *
 * @param signUseCase Юзкейс регистрации
 * @param tokenStorage Хранилище токенов
 */
class SignViewModel(
    private val signUseCase: SignUseCase,
    private val tokenStorage: TokenStorage,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiStateSign = MutableStateFlow(UiStateSign())
    val uiStateSign: StateFlow<UiStateSign>
        get() = _uiStateSign.asStateFlow()

    private val _events = MutableSharedFlow<SignEffect>()
    val events: SharedFlow<SignEffect>
        get() = _events.asSharedFlow()

    fun processCommand(command: SignCommand) {
        when (val command = command) {
            is SignCommand.ChangeName -> {
                _uiStateSign.update { previousState ->
                    previousState.copy(name = command.name)
                }
            }

            is SignCommand.ChangeEmail -> {
                _uiStateSign.update { previousState ->
                    previousState.copy(email = command.email)
                }
            }

            is SignCommand.ChangePassword -> {
                _uiStateSign.update { previousState ->
                    previousState.copy(password = command.password)
                }
            }

            SignCommand.ClickSignUp -> {
                viewModelScope.launch(dispatcherProvider.default) {
                    val state = _uiStateSign.value

                    if (state.password == state.confirmedPassword) {
                        val result = signUseCase(
                            name = _uiStateSign.value.name,
                            email = _uiStateSign.value.email,
                            password = _uiStateSign.value.password
                        )

                        when (result) {
                            is AuthResult.Error<*> -> {
                                _events.emit(ShowError(result.errorData.message))
                            }

                            is AuthResult.Success<UserSession> -> {
                                val tokens = result.data.tokens
                                tokenStorage.saveToken(token = tokens.accessToken, isAccess = true)
                                tokenStorage.saveToken(token = tokens.refreshToken, isAccess = false)
                            }
                        }
                    } else {
                        _events.emit(ShowError("Пароли не совпадают"))
                    }
                }
            }

            SignCommand.ChangePasswordVisibility -> {
                _uiStateSign.update { previousState ->
                    previousState.copy(isPasswordVisible = !previousState.isPasswordVisible)
                }
            }

            is SignCommand.ChangeConfirmedPassword -> {
                _uiStateSign.update { previousState ->
                    previousState.copy(confirmedPassword = command.confirmedPassword)
                }
            }

            SignCommand.ChangeConfirmedPasswordVisibility -> {
                _uiStateSign.update { previousState ->
                    previousState.copy(isConfirmedPasswordVisible = !previousState.isConfirmedPasswordVisible)
                }
            }

            SignCommand.ClickLogin -> {
                // TODO NAVIGATE EVENT
            }
        }
    }
}

/**
 * Команды для вьюмодели регистрации
 *
 * @see ChangeName Сменить имя
 * @see ChangeEmail Сменить почту
 * @see ChangePassword Сменить пароль
 * @see ChangeConfirmedPassword Сменить подтвержденный пароль
 * @see ChangePasswordVisibility Сменить видимость пароля
 * @see ChangeConfirmedPasswordVisibility Сменить видимость подтвержденного пароля
 * @see ClickSignUp Нажатие на кнопку Login
 */
sealed interface SignCommand {
    data class ChangeName(val name: String) : SignCommand
    data class ChangeEmail(val email: String) : SignCommand
    data class ChangePassword(val password: String) : SignCommand
    data class ChangeConfirmedPassword(val confirmedPassword: String) : SignCommand
    data object ClickLogin : SignCommand
    data object ChangePasswordVisibility : SignCommand
    data object ChangeConfirmedPasswordVisibility : SignCommand
    data object ClickSignUp : SignCommand
}

/**
 * Состояние экрана регистрации
 *
 * @see name Текущее имя
 * @see email Текущая почта
 * @see password Текущий пароль
 * @see confirmedPassword Текущий подтвержденный пароль
 * @see isPasswordVisible Видимость пароля
 * @see isConfirmedPasswordVisible Видимость подтвержденного пароля
 */
data class UiStateSign(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmedPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmedPasswordVisible: Boolean = false,
)

sealed interface SignEffect {
    data class ShowError(val message: String) : SignEffect
}