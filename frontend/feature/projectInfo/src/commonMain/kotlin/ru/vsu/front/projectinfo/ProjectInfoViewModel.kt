package ru.vsu.front.projectinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.*
import ru.vsu.front.model.entity.*
import ru.vsu.front.projectinfo.ProjectInfoEffect.ProjectDeleted
import ru.vsu.front.projectinfo.ProjectInfoEffect.ShowMessage

/**
 * Вьюмодель экрана кода проекта.
 *
 * @param projectId Идентификатор открытого проекта.
 * @param observeFilesUseCase UseCase для подписки на деревья файлов проекта.
 * @param observeRemovedFromProjectUseCase UseCase для подписки на события исключения текущего пользователя из проекта.
 * @param connectToTheProjectRoomUseCase UseCase подключения к комнате проекта.
 * @param leaveFromProjectRoomUseCase UseCase для выхода из комнаты проекта.
 * @param getProjectInfoUseCase UseCase для получения информации о проекте.
 * @param deleteProjectUseCase UseCase для удаления проекта.
 * @param kickUserUseCase UseCase для исключения пользователя из проекта.
 * @param inviteUserUseCase UseCase для приглашения пользователя в проект.
 * @param dispatcherProvider Провайдер корутинных диспатчеров.
 */
class ProjectInfoViewModel(
    private val projectId: Int,
    private val observeFilesUseCase: ObserveFilesUseCase,
    private val observeRemovedFromProjectUseCase: ObserveRemovedFromProjectUseCase,
    private val connectToTheProjectRoomUseCase: ConnectToTheProjectRoomUseCase,
    private val leaveFromProjectRoomUseCase: LeaveFromProjectRoomUseCase,
    private val getProjectInfoUseCase: GetProjectInfoUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val kickUserUseCase: KickUserUseCase,
    private val inviteUserUseCase: InviteUserUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            loadProjectInfo()
            connectToTheProjectRoomUseCase(projectId)
            observeFilesUseCase(projectId)
                .flowOn(dispatcherProvider.io)
                .onEach { files ->
                    _uiState.update { previousState ->
                        if (previousState is UiStatusProjectInfo.Loaded) {
                            UiStatusProjectInfo.Loaded(
                                UiStateProjectInfo(
                                    projectInfo = previousState.uiStatusProjectInfo.projectInfo,
                                    projectFiles = files,
                                )
                            )
                        } else {
                            UiStatusProjectInfo.Loaded(
                                UiStateProjectInfo(
                                    projectFiles = files,
                                )
                            )
                        }
                    }
                }
                .launchIn(viewModelScope)
            observeRemovedFromProjectUseCase()
                .flowOn(dispatcherProvider.io)
                .onEach { projectIdRemovedFrom ->
                    if (projectIdRemovedFrom == projectId) {
                        leaveFromProjectRoomUseCase(projectId)
                        _events.emit(ProjectInfoEffect.RemovedFromCurrentProject)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private val _uiState = MutableStateFlow<UiStatusProjectInfo>((UiStatusProjectInfo.Loading))
    val uiState = _uiState.asStateFlow()

    /**
     * Поток одноразовых событий для UI.
     */
    private val _events = MutableSharedFlow<ProjectInfoEffect>()
    val events: SharedFlow<ProjectInfoEffect>
        get() = _events.asSharedFlow()

    fun processCommand(command: ProjectInfoCommand) {
        when (command) {
            ProjectInfoCommand.ClickDeleteProject -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    when (val result = deleteProjectUseCase(projectId)) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict,
                                is RequestError.Forbidden,
                                is RequestError.NetworkException,
                                is RequestError.UnknownError -> {
                                    val errorMessage = requestError.message
                                    _events.emit(ShowMessage(message = errorMessage))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _events.emit(ProjectDeleted)
                        }
                    }
                }
            }

            ProjectInfoCommand.ClickRepeatLoadingProjectInfo -> {
                loadProjectInfo()
            }

            ProjectInfoCommand.ClickToggleMembersVisibility -> {
                _uiState.update { previousState ->
                    if (previousState is UiStatusProjectInfo.Loaded) {
                        val areMembersVisible = previousState.uiStatusProjectInfo.areMembersVisible
                        previousState.copy(
                            uiStatusProjectInfo = previousState.uiStatusProjectInfo.copy(
                                areMembersVisible = !areMembersVisible
                            )
                        )
                    } else {
                        previousState
                    }
                }
            }

            is ProjectInfoCommand.ChangeInputtedUserEmail -> {
                _uiState.update { previousState ->
                    if (previousState is UiStatusProjectInfo.Loaded) {
                        previousState.copy(
                            uiStatusProjectInfo = previousState.uiStatusProjectInfo.copy(
                                inputtedUserEmail = command.email
                            )
                        )
                    } else {
                        previousState
                    }
                }
            }

            is ProjectInfoCommand.ClickKickMember -> {
                _uiState.update { previousState ->
                    if (previousState !is UiStatusProjectInfo.Loaded) return
                    val newState = previousState.copy(
                        uiStatusProjectInfo = previousState.uiStatusProjectInfo.copy(
                            isInviteUserDialogVisible = false
                        )
                    )
                    newState
                }
                viewModelScope.launch(dispatcherProvider.io) {
                    when (val result = kickUserUseCase(
                        userEmail = command.email,
                        projectId = projectId
                    )) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict,
                                is RequestError.Forbidden,
                                is RequestError.NetworkException,
                                is RequestError.UnknownError -> {
                                    val errorMessage = requestError.message
                                    _events.emit(ShowMessage(message = errorMessage))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _uiState.update { previousState ->
                                if (previousState !is UiStatusProjectInfo.Loaded) return@launch
                                val newUsersList =
                                    previousState.uiStatusProjectInfo.projectInfo.users.filter {
                                        it.email != command.email
                                    }
                                val newState = previousState.copy(
                                    uiStatusProjectInfo = previousState.uiStatusProjectInfo.copy(
                                        projectInfo = previousState.uiStatusProjectInfo.projectInfo.copy(users = newUsersList),
                                        inputtedUserEmail = ""
                                    )
                                )
                                _events.emit(ShowMessage(message = "Пользователь успешно исключён"))
                                newState
                            }
                        }
                    }
                }
            }

            ProjectInfoCommand.ClickInviteMember -> {
                val uiState = _uiState.value
                if (uiState !is UiStatusProjectInfo.Loaded) return
                _uiState.update {
                    val newState = uiState.copy(
                        uiStatusProjectInfo = uiState.uiStatusProjectInfo.copy(
                            isInviteUserDialogVisible = false
                        )
                    )
                    newState
                }
                viewModelScope.launch(dispatcherProvider.io) {
                    when (val result = inviteUserUseCase(
                        userEmail = uiState.uiStatusProjectInfo.inputtedUserEmail,
                        projectId = projectId
                    )) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict,
                                is RequestError.Forbidden,
                                is RequestError.NetworkException,
                                is RequestError.UnknownError -> {
                                    val errorMessage = requestError.message
                                    _events.emit(ShowMessage(message = errorMessage))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<User> -> {
                            _events.emit(ShowMessage(message = "Пользователь успешно приглашён"))
                            _uiState.update { previousState ->
                                if (previousState !is UiStatusProjectInfo.Loaded) return@launch
                                val newUsersList = previousState.uiStatusProjectInfo.projectInfo.users + result.data
                                val newState = previousState.copy(
                                    uiStatusProjectInfo = previousState.uiStatusProjectInfo.copy(
                                        projectInfo = previousState.uiStatusProjectInfo.projectInfo.copy(users = newUsersList),
                                        inputtedUserEmail = ""
                                    )
                                )
                                newState
                            }
                        }
                    }
                }
            }

            ProjectInfoCommand.ClickCloseInviteUserDialogVisible -> {
                val uiState = _uiState.value
                if (uiState !is UiStatusProjectInfo.Loaded) return
                _uiState.update {
                    uiState.copy(
                        uiStatusProjectInfo = uiState.uiStatusProjectInfo.copy(
                            isInviteUserDialogVisible = false
                        )
                    )
                }
            }

            ProjectInfoCommand.ClickCloseKickUserDialogVisible -> {
                val uiState = _uiState.value
                if (uiState !is UiStatusProjectInfo.Loaded) return
                _uiState.update {
                    uiState.copy(
                        uiStatusProjectInfo = uiState.uiStatusProjectInfo.copy(
                            isKickUserDialogVisible = false
                        )
                    )
                }
            }

            ProjectInfoCommand.ClickToggleInviteUserDialogVisible -> {
                val uiState = _uiState.value
                if (uiState !is UiStatusProjectInfo.Loaded) return
                val isInviteUserDialogVisible = uiState.uiStatusProjectInfo.isInviteUserDialogVisible
                _uiState.update {
                    uiState.copy(
                        uiStatusProjectInfo = uiState.uiStatusProjectInfo.copy(
                            isInviteUserDialogVisible = !isInviteUserDialogVisible
                        )
                    )
                }
            }

            ProjectInfoCommand.ClickToggleKickUserDialogVisible -> {
                val uiState = _uiState.value
                if (uiState !is UiStatusProjectInfo.Loaded) return
                val isKickUserDialogVisible = uiState.uiStatusProjectInfo.isKickUserDialogVisible
                _uiState.update {
                    uiState.copy(
                        uiStatusProjectInfo = uiState.uiStatusProjectInfo.copy(
                            isKickUserDialogVisible = !isKickUserDialogVisible
                        )
                    )
                }
            }
        }
    }

    private fun loadProjectInfo() {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = getProjectInfoUseCase(projectId)) {
                is Response.Error<*> -> {
                    when (result.requestError) {
                        is RequestError.Conflict,
                        is RequestError.Forbidden,
                        is RequestError.NetworkException,
                        is RequestError.UnknownError -> {
                            _uiState.update { previousState ->
                                if (previousState is UiStatusProjectInfo.Loaded) {
                                    previousState.copy(
                                        uiStatusProjectInfo = previousState
                                            .uiStatusProjectInfo
                                            .copy(projectInfoErrorLoading = true)
                                    )
                                } else {
                                    UiStatusProjectInfo.Loaded(
                                        UiStateProjectInfo(
                                            projectInfoErrorLoading = true
                                        )
                                    )
                                }
                            }
                        }

                        else -> {
                        }
                    }
                }

                is Response.Success<ProjectInfo> -> {
                    val projectInfo = result.data
                    _uiState.update { previousState ->
                        if (previousState is UiStatusProjectInfo.Loaded) {
                            UiStatusProjectInfo.Loaded(
                                UiStateProjectInfo(
                                    projectInfo = projectInfo,
                                    projectFiles = previousState.uiStatusProjectInfo.projectFiles,
                                    projectInfoErrorLoading = false
                                )
                            )
                        } else {
                            UiStatusProjectInfo.Loaded(
                                UiStateProjectInfo(
                                    projectInfo = projectInfo,
                                    projectInfoErrorLoading = false
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCleared() {
        super.onCleared()
        GlobalScope.launch {
            leaveFromProjectRoomUseCase(projectId)
        }
    }
}

/**
 * Состояние экрана информации о проекте.
 */
data class UiStateProjectInfo(
    val projectInfo: ProjectInfo = ProjectInfo(
        languageName = "",
        projectId = 0,
        projectName = "",
        isUserOwner = false,
        users = emptyList()
    ),
    val projectFiles: List<FileNode> = emptyList(),
    val projectInfoErrorLoading: Boolean = false,
    val areMembersVisible: Boolean = true,
    val inputtedUserEmail: String = "",
    val isInviteUserDialogVisible: Boolean = false,
    val isKickUserDialogVisible: Boolean = false,
)


/**
 * Текущий статус экрана.
 */
sealed interface UiStatusProjectInfo {
    /**
     * Данные загружаются.
     */
    data object Loading : UiStatusProjectInfo

    /**
     * Данные загружены.
     */
    data class Loaded(val uiStatusProjectInfo: UiStateProjectInfo) : UiStatusProjectInfo
}

/**
 * Команды.
 */
sealed interface ProjectInfoCommand {
    data object ClickDeleteProject : ProjectInfoCommand
    data object ClickRepeatLoadingProjectInfo : ProjectInfoCommand
    data object ClickToggleMembersVisibility : ProjectInfoCommand
    data class ClickKickMember(val email: String) : ProjectInfoCommand
    data object ClickInviteMember : ProjectInfoCommand
    data object ClickToggleInviteUserDialogVisible : ProjectInfoCommand
    data object ClickCloseInviteUserDialogVisible : ProjectInfoCommand
    data object ClickToggleKickUserDialogVisible : ProjectInfoCommand
    data object ClickCloseKickUserDialogVisible : ProjectInfoCommand
    data class ChangeInputtedUserEmail(val email: String) : ProjectInfoCommand
}

/**
 * События.
 */
sealed interface ProjectInfoEffect {
    data object ProjectDeleted : ProjectInfoEffect
    data object RemovedFromCurrentProject : ProjectInfoEffect
    data class ShowMessage(val message: String) : ProjectInfoEffect
}