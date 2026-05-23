package ru.vsu.front.projectinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.ConnectToTheProjectRoomUseCase
import ru.vsu.front.domain.usecase.DeleteProjectUseCase
import ru.vsu.front.domain.usecase.GetProjectInfoUseCase
import ru.vsu.front.domain.usecase.ObserveFilesUseCase
import ru.vsu.front.model.entity.FileNode
import ru.vsu.front.model.entity.ProjectInfo
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.projectinfo.ProjectInfoEffect.*

class ProjectInfoViewModel(
    private val projectId: Int,
    private val observeFilesUseCase: ObserveFilesUseCase,
    private val connectToTheProjectRoomUseCase: ConnectToTheProjectRoomUseCase,
    private val getProjectInfoUseCase: GetProjectInfoUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    init {
        viewModelScope.launch(dispatcherProvider.io) {
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
            loadProjectInfo()
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
                            _events.emit(ProjectInfoEffect.ProjectDeleted)
                        }
                    }
                }
            }

            ProjectInfoCommand.ClickRepeatLoadingProjectInfo -> {
                loadProjectInfo()
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
}

data class UiStateProjectInfo(
    val projectInfo: ProjectInfo = ProjectInfo(
        languageName = "",
        projectId = 0,
        projectName = "",
        isUserOwner = false,
        users = emptyList()
    ),
    val projectFiles: List<FileNode> = emptyList(),
    val projectInfoErrorLoading: Boolean = false
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
}


/**
 * События.
 */
sealed interface ProjectInfoEffect {
    data object ProjectDeleted : ProjectInfoEffect
    data class ShowMessage(val message: String) : ProjectInfoEffect
}