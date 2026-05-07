package ru.vsu.front.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.DeleteNotificationUseCase
import ru.vsu.front.domain.usecase.ObserveNotificationsUseCase
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response

/**
 * Вьюмодель экрана уведомлений.
 *
 * @param deleteNotificationUseCase UseCase для удаления уведомлений.
 * @param observeNotificationsUseCase UseCase для подписи на уведомления.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
 */
class NotificationsViewModel(
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiStateNotifications>(UiStateNotifications.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NotificationsEffect>()
    val events: SharedFlow<NotificationsEffect>
        get() = _events.asSharedFlow()

    private var observeJob: Job? = null

    init {
        observeNotifications()
    }

    fun processCommand(command: NotificationsCommand) {
        when (command) {
            NotificationsCommand.RetryObservingNotifications -> observeNotifications()

            is NotificationsCommand.DeleteNotification -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    updateLoadedState {
                        it.toMutableList().apply {
                            removeIf { notification ->
                                notification.notificationId == command.notificationId
                            }
                        }
                    }
                    when (val result = deleteNotificationUseCase(command.notificationId)) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict -> _events.emit(NotificationsEffect.ShowMessage(requestError.message))
                                is RequestError.Forbidden -> _events.emit(NotificationsEffect.ShowMessage(requestError.message))
                                is RequestError.NetworkException -> _events.emit(NotificationsEffect.ShowMessage(requestError.message))
                                is RequestError.UnknownError -> _events.emit(NotificationsEffect.ShowMessage(requestError.message))
                                else -> {
                                    // Nothing
                                }
                            }
                        }

                        is Response.Success<*> -> {
                        }
                    }
                }
            }
        }
    }

    private fun observeNotifications() {
        _uiState.update { UiStateNotifications.Loading }

        observeJob = viewModelScope.launch {

            val timeoutJob = launch {
                delay(20000)
                if (_uiState.value is UiStateNotifications.Loading) {
                    observeJob?.cancel()
                    _uiState.update { UiStateNotifications.Error }
                }
            }

            observeNotificationsUseCase()
                .flowOn(dispatcherProvider.io)
                .onEach { notifications ->
                    timeoutJob.cancel()

                    _uiState.update {
                        UiStateNotifications.Loaded(notifications = notifications)
                    }
                }
                .catch {
                    timeoutJob.cancel()
                    _uiState.update {
                        UiStateNotifications.Error
                    }
                }
                .collect()
        }
    }

    /**
     * Обновление состояния, если оно текущее состояние Loaded.
     */
    private inline fun updateLoadedState(transform: (List<Notification>) -> List<Notification>) {
        _uiState.update { currentState ->
            if (currentState is UiStateNotifications.Loaded) {
                UiStateNotifications.Loaded(transform(currentState.notifications))
            } else {
                currentState
            }
        }
    }
}

/**
 * Текущий статус экрана.
 */
sealed interface UiStateNotifications {
    /**
     * Данные загружаются.
     */
    data object Loading : UiStateNotifications

    /**
     * Данные загружены.
     */
    data class Loaded(val notifications: List<Notification>) : UiStateNotifications

    /**
     * Ошибка при загрузке.
     */
    data object Error : UiStateNotifications
}

/**
 * Команды.
 */
sealed interface NotificationsCommand {
    data object RetryObservingNotifications : NotificationsCommand
    data class DeleteNotification(val notificationId: Int) : NotificationsCommand
}

/**
 * События экрана уведомлений.
 */
sealed interface NotificationsEffect {
    /**
     * Показывает сообщение на экране.
     */
    data class ShowMessage(val message: String) : NotificationsEffect
}