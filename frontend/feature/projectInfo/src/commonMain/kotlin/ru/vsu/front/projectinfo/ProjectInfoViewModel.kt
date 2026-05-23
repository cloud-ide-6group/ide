package ru.vsu.front.projectinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.ConnectToTheProjectRoomUseCase
import ru.vsu.front.domain.usecase.ObserveFilesUseCase
import ru.vsu.front.model.entity.FileNode

class ProjectInfoViewModel(
    private val projectId: Int,
    private val observeFilesUseCase: ObserveFilesUseCase,
    private val connectToTheProjectRoomUseCase: ConnectToTheProjectRoomUseCase,
    private val dispatcherProvider: DispatcherProvider,
): ViewModel() {

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            connectToTheProjectRoomUseCase(projectId)
            observeFilesUseCase(projectId)
                .flowOn(dispatcherProvider.io)
                .onEach { files ->
                    println(files.toString())
                    _uiState.update {
                        it.copy(projectFiles = files)
                    }
                }
                .launchIn(viewModelScope)
        }
    }
    private val _uiState = MutableStateFlow(ProjectInfoState())
    val uiState = _uiState.asStateFlow()
}

data class ProjectInfoState(
    val projectFiles: List<FileNode> = emptyList()
)