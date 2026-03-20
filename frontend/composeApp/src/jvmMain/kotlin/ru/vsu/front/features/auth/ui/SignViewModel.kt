package ru.vsu.front.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.di.dispatcher_provider.DispatcherProvider
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.usecase.SignUseCase
import ru.vsu.front.features.auth.ui.SignEffect.ShowError

/**
 * Вьюмодель экрана регистрации.
 *
 * @param signUseCase UseCase для запроса регистрации.
 * @param tokenStorage Локальное хранилище для сохранения JWT-токенов.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
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
        }
    }
}

/**
 * Набор команд экрана регистрации.
 * Описывает все взаимодействия пользователя с формой.
 */
sealed interface SignCommand {
    /**
     * Команда обновления введенного текста в поле "Имя".
     * */
    data class ChangeName(val name: String) : SignCommand

    /**
     * Команда обновления введенного текста в поле "Почта".
     * */
    data class ChangeEmail(val email: String) : SignCommand

    /**
     * Команда обновления введенного текста в основном поле "Пароль".
     * */
    data class ChangePassword(val password: String) : SignCommand

    /**
     * Команда обновления введенного текста в поле "Подтверждение пароля".
     * */
    data class ChangeConfirmedPassword(val confirmedPassword: String) : SignCommand

    /**
     * Команда переключения видимости основного пароля.
     * */
    data object ChangePasswordVisibility : SignCommand

    /**
     * Команда переключения видимости подтвержденного пароля.
     * */
    data object ChangeConfirmedPasswordVisibility : SignCommand

    /**
     * Команда отправки формы на сервер.
     * */
    data object ClickSignUp : SignCommand
}

/**
 * Состояние экрана регистрации.
 *
 * @property name Текущее введенное имя.
 * @property email Текущая введенная почта.
 * @property password Текущий введенный основной пароль.
 * @property confirmedPassword Текущий введенный пароль-подтверждение.
 * @property isPasswordVisible Видимость основного пароля.
 * @property isConfirmedPasswordVisible Видимость пароля-подтверждения.
 */
data class UiStateSign(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmedPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmedPasswordVisible: Boolean = false,
)

/**
 * События экрана регистрации.
 */
sealed interface SignEffect {
    /**
     * Событие показа уведомления об ошибке.
     * @property message Сообщение ошибки.
     */
    data class ShowError(val message: String) : SignEffect
}