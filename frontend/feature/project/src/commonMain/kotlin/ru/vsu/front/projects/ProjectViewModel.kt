package ru.vsu.front.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.*
import ru.vsu.front.model.entity.FileNode
import ru.vsu.front.model.entity.Message
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.projects.ProjectCommand.*
import ru.vsu.front.projects.ProjectEffect.ShowMessage

/**
 * Вьюмодель экрана кода проекта.
 *
 * @param projectId Идентификатор открытого проекта.
 * @param observeFilesUseCase UseCase для получения списка файлов проекта.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
 * @param connectToTheProjectRoomUseCase UseCase подключения к комнате проекта.
 * @param createFileUseCase UseCase для создания файла.
 * @param deleteFileUseCase UseCase для удаления файлаа.
 * @param renameFileUseCase UseCase для переименовывания файла.
 * @param observeFileContentUseCase UseCase для подписки на текущее содержимое файла.
 * @param updateFileContentUseCase UseCase для обновления текущего содержимого файла (текста).
 * @param runCodeUseCase UseCase для запуска кода.
 * @param stopCodeUseCase UseCase для остановки выполняемой программы.
 * @param sendInputUseCase UseCase для отправки ввода в терминал.
 * @param createChatUseCase UseCase для создания чата.
 * @param joinChatUseCase UseCase для подключения к чату.
 * @param leaveChatUseCase UseCase для отключения от чата.
 * @param createMessageUseCase UseCase для создания (отправки) сообщения.
 * @param observeMessagesUseCase UseCase для подписки на сообщения чата.
 * @param observeRemovedFromProjectUseCase UseCase для отслеживания события исключения пользователя из проекта.
 * @param leaveFromProjectRoomUseCase UseCase для отключения от комнаты проекта.
 * @param observeConsoleOutputUseCase UseCase для подписки на вывод выполняемой программы.
 * @param getFileContentUseCase UseCase получения текущего содержимого определенного файла.
 */
@OptIn(FlowPreview::class)
class ProjectViewModel(
    private val projectId: Int,
    private val observeFilesUseCase: ObserveFilesUseCase,
    private val dispatcherProvider: DispatcherProvider,
    private val connectToTheProjectRoomUseCase: ConnectToTheProjectRoomUseCase,
    private val createFileUseCase: CreateFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val renameFileUseCase: RenameFileUseCase,
    private val observeFileContentUseCase: ObserveFileContentUseCase,
    private val updateFileContentUseCase: UpdateFileContentUseCase,
    private val runCodeUseCase: RunCodeUseCase,
    private val stopCodeUseCase: StopCodeUseCase,
    private val sendInputUseCase: SendInputUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val joinChatUseCase: JoinChatUseCase,
    private val leaveChatUseCase: LeaveChatUseCase,
    private val createMessageUseCase: CreateMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
    private val observeRemovedFromProjectUseCase: ObserveRemovedFromProjectUseCase,
    private val leaveFromProjectRoomUseCase: LeaveFromProjectRoomUseCase,
    private val observeConsoleOutputUseCase: ObserveConsoleOutputUseCase,
    private val getFileContentUseCase: GetFileContentUseCase
) : ViewModel() {

    /**
     * Состояние экрана.
     */
    private val _uiState = MutableStateFlow(ProjectState())
    val uiState = _uiState.asStateFlow()

    /**
     * Поток одноразовых событий для UI.
     */
    private val _events = MutableSharedFlow<ProjectEffect>()
    val events: SharedFlow<ProjectEffect>
        get() = _events.asSharedFlow()

    /**
     * Фоновая задача для отложенной отправки изменений кода на сервер (debounce),
     * чтобы не отправлять данные при каждом нажатии клавиши.
     */
    private var debounceUploadJob: Job? = null

    /**
     * Фоновая задача для подписки на новые сообщения открытого чата.
     */
    private var observeMessagesJob: Job? = null

    init {
        viewModelScope.launch {
            connectToTheProjectRoomUseCase(projectId)
            observeFilesUseCase(projectId)
                .flowOn(dispatcherProvider.io)
                .onEach { files ->
                    _uiState.update {
                        it.copy(
                            files = files,
                            recentlyFiles = it.recentlyFiles.filter { file -> file in files }.toSet()
                        )
                    }
                }
                .launchIn(viewModelScope)
            observeConsoleOutputUseCase(projectId)
                .flowOn(dispatcherProvider.io)
                .onEach { consoleOutput ->
                    _uiState.update {
                        it.copy(
                            consoleLines = it.consoleLines + consoleOutput.text,
                            isProgramEnded = consoleOutput.isProgramEnded
                        )
                    }
                }
                .launchIn(viewModelScope)
            observeRemovedFromProjectUseCase()
                .flowOn(dispatcherProvider.io)
                .onEach {
                    leaveFromProjectRoomUseCase(projectId)
                    _events.emit(ProjectEffect.RemovedFromProject)
                }
                .launchIn(viewModelScope)
            observeFileContentUseCase()
                .flowOn(dispatcherProvider.io)
                .onEach { fileContent ->
                    if (fileContent.id != _uiState.value.selectedFileId) return@onEach
                    _uiState.update {
                        it.copy(selectedFileContent = fileContent.content)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    /**
     * Выполняет определенные действия в зависимости от переданной команды.
     */
    fun processCommand(command: ProjectCommand) {
        when (command) {
            CloseCreateFileDialog -> {
                _uiState.update {
                    it.copy(isCreateFileDialogOpen = false)
                }
            }

            is OpenCreateFileDialog -> {
                _uiState.update {
                    it.copy(
                        parentIdInCreatingFileDialog = command.parentId,
                        isCreateFileDialogOpen = true
                    )
                }
            }

            is ChangeFileNameForCreate -> {
                _uiState.update {
                    it.copy(fileNameForCreate = command.name)
                }
            }

            ClickFileSelectedInCreatingFileDialog -> {
                _uiState.update {
                    it.copy(isFolderSelectedInCreatingFileDialog = false)
                }
            }

            ClickFolderSelectedInCreatingFileDialog -> {
                _uiState.update {
                    it.copy(isFolderSelectedInCreatingFileDialog = true)
                }
            }

            is ClickCreateFile -> {
                val uiState = _uiState.value
                _uiState.update {
                    it.copy(
                        isCreateFileDialogOpen = false,
                        isFolderSelectedInCreatingFileDialog = false,
                        fileNameForCreate = ""
                    )
                }
                viewModelScope.launch(dispatcherProvider.io) {
                    val result = createFileUseCase(
                        fileName = uiState.fileNameForCreate,
                        projectId = projectId,
                        isFolder = uiState.isFolderSelectedInCreatingFileDialog,
                        parentId = command.parentId,
                    )

                    when (result) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict,
                                is RequestError.Forbidden,
                                is RequestError.NetworkException,
                                is RequestError.UnknownError -> _events.emit(ShowMessage(requestError.message))

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _events.emit(ShowMessage(message = "Файл создан"))
                        }
                    }
                }
            }

            is ClickDeleteFile -> {
                val deletedIds = findNodeAndDescendantsIds(_uiState.value.files, command.fileId)

                viewModelScope.launch(dispatcherProvider.io) {
                    when (val result = deleteFileUseCase(fileId = command.fileId)) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict,
                                is RequestError.Forbidden,
                                is RequestError.NetworkException,
                                is RequestError.UnknownError -> {
                                    _events.emit(ShowMessage(requestError.message))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _events.emit(ShowMessage(message = "Файл удалён"))
                            removeDeletedFilesFromUI(deletedIds)
                        }
                    }
                }
            }

            is OpenRenameFileDialog -> {
                _uiState.update {
                    it.copy(
                        isRenameFileDialogOpen = true,
                        fileIdInRenamingFileDialog = command.fileId,
                    )
                }
            }

            CloseRenameFileDialog -> {
                _uiState.update {
                    it.copy(isRenameFileDialogOpen = false)
                }
            }

            is ClickRenameFile -> {
                _uiState.update {
                    it.copy(
                        isRenameFileDialogOpen = false,
                        fileNameForRename = ""
                    )
                }
                viewModelScope.launch(dispatcherProvider.io) {
                    when (val result = renameFileUseCase(fileId = command.fileId, newName = command.newName)) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict,
                                is RequestError.Forbidden,
                                is RequestError.NetworkException,
                                is RequestError.UnknownError -> {
                                    _events.emit(ShowMessage(requestError.message))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _events.emit(ShowMessage(message = "Файл переименован"))
                        }
                    }
                }
            }

            is ChangeFileNameForRename -> {
                _uiState.update {
                    it.copy(fileNameForRename = command.name)
                }
            }

            is ClickFile -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    with(_uiState.value) {
                        if (_uiState.value.selectedFileId == command.fileId) return@launch

                        if (command.fileId != selectedFileId) {
                            observeMessagesJob?.cancel()

                            if (activeChatLink != null) {
                                leaveChatUseCase(activeChatLink, projectId)
                            }

                            _uiState.update { previousState ->
                                previousState.copy(activeChatId = null, activeChatLink = null, messages = emptyList())
                            }
                        }
                    }

                    getFileContent(command.fileId)
                }
            }

            is UpdateFileContent -> {
                _uiState.update {
                    if (it.selectedFileId == command.fileId) {
                        it.copy(selectedFileContent = command.content)
                    } else it
                }

                debounceUploadJob?.cancel()
                debounceUploadJob = viewModelScope.launch(dispatcherProvider.io) {
                    delay(1000)
                    updateFileContentUseCase(
                        fileId = command.fileId,
                        content = command.content
                    )
                }
            }

            ClickRunCode -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    runCodeUseCase(projectId = projectId)
                }
            }

            ClickStopCode -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    stopCodeUseCase(projectId = projectId)
                }
            }

            ClickClearTerminal -> {
                _uiState.update {
                    it.copy(consoleLines = emptyList())
                }
            }

            is ChangeTerminalInput -> {
                _uiState.update {
                    it.copy(terminalInput = command.input)
                }
            }

            SendInputToTerminalFromText -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    _uiState.update {
                        val input = it.terminalInput
                        sendInputUseCase(input, projectId)
                        it.copy(terminalInput = "")
                    }
                }
            }

            is ClickDeleteFileFromRecentlyFiles -> {
                removeDeletedFilesFromUI(setOf(command.fileId))
            }

            is ClickChat -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val identificator = command.identificator.drop(2)

                    _uiState.update {
                        it.activeChatLink?.let { previousChatLink ->
                            val previousChatLink = previousChatLink.drop(2)
                            leaveChatUseCase(previousChatLink, projectId)
                        }
                        it.copy(activeChatLink = identificator)
                    }

                    observeMessages()

                    createChatUseCase(projectId, identificator)

                    joinChatUseCase(identificator, projectId)
                }
            }

            is ChangeChatInput -> {
                _uiState.update {
                    it.copy(chatInputValue = command.input)
                }
            }

            CloseChat -> {
                _uiState.update {
                    it.activeChatLink?.let { chatLink ->
                        viewModelScope.launch(dispatcherProvider.io) {
                            leaveChatUseCase(chatLink, projectId)
                        }
                    }

                    it.copy(activeChatLink = null, chatInputValue = "", messages = emptyList())
                }
            }

            CreateMessage -> {
                val state = _uiState.value
                state.activeChatId?.let { chatId ->
                    viewModelScope.launch(dispatcherProvider.io) {
                        val result = createMessageUseCase(chatId, state.chatInputValue)
                        when (result) {
                            is Response.Error<*> -> {
                                _events.emit(ShowMessage("Не удалось отправить сообщение"))
                            }

                            is Response.Success<*> -> {
                            }
                        }
                    }
                    _uiState.update {
                        it.copy(chatInputValue = "")
                    }
                }
            }

            CloseTerminalPanel -> {
                _uiState.update {
                    it.copy(isTerminalPanelVisible = false)
                }
            }

            ChangeTerminalPanelVisible -> {
                _uiState.update {
                    it.copy(isTerminalPanelVisible = !it.isTerminalPanelVisible)
                }
            }
        }
    }

    /**
     * Выполняет запрос на получение текущего содержимого файла.
     *
     * * @param fileId Идентификатор файла.
     */
    private suspend fun getFileContent(fileId: Int) {
        _uiState.update { previousState ->
            val flatFiles = previousState.files.flattenAll()

            previousState.copy(
                selectedFileId = fileId,
                recentlyFiles = (previousState.recentlyFiles + flatFiles.first { it.id == fileId }).toSet(),
            )
        }

        getFileContentUseCase(fileId)
    }

    /**
     * Устанавливает подписку на новые сообщения текущего активного чата.
     */
    private fun observeMessages() {
        observeMessagesJob?.cancel()

        observeMessagesJob = observeMessagesUseCase()
            .flowOn(dispatcherProvider.io)
            .onEach { (chatId, messages) ->
                val sortedBySendTimeMessages = messages.sortedBy { it.sendTime }

                _uiState.update {
                    it.copy(activeChatId = chatId, messages = sortedBySendTimeMessages)
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Преобразует лист деревьев в прямой лист.
     */
    private fun List<FileNode>.flattenAll(): List<FileNode> {
        return flatMap { node ->
            listOf(node.copy(children = emptyList())) +
                    node.children.flattenAll()
        }
    }

    /**
     * Собирает идентификаторы узла и всех его потомков.
     */
    private fun findNodeAndDescendantsIds(nodes: List<FileNode>, targetId: Int): Set<Int> {
        val result = mutableSetOf<Int>()

        fun findNode(list: List<FileNode>): FileNode? {
            for (node in list) {
                if (node.id == targetId) return node
                val found = findNode(node.children)
                if (found != null) return found
            }
            return null
        }

        val targetNode = findNode(nodes) ?: return emptySet()

        fun collectIds(node: FileNode) {
            result.add(node.id)
            node.children.forEach { collectIds(it) }
        }

        collectIds(targetNode)
        return result
    }

    /**
     * Удаляет переданный список файлов из панели недавних, если активный файл оказался среди удаленных.
     */
    private fun removeDeletedFilesFromUI(deletedIds: Set<Int>) {
        if (deletedIds.isEmpty()) return

        val currentState = _uiState.value
        val isClosingSelectedFile = currentState.selectedFileId in deletedIds
        val newRecentlyFiles = currentState.recentlyFiles.filter { it.id !in deletedIds }.toSet()

        _uiState.update {
            it.copy(recentlyFiles = newRecentlyFiles)
        }

        if (isClosingSelectedFile) {
            val fileToSelectNext = newRecentlyFiles.firstOrNull()

            if (fileToSelectNext != null) {
                processCommand(ClickFile(fileToSelectNext.id))
            } else {
                val activeLink = currentState.activeChatLink
                if (activeLink != null) {
                    viewModelScope.launch(dispatcherProvider.io) {
                        leaveChatUseCase(activeLink, projectId)
                    }
                }

                _uiState.update {
                    it.copy(
                        selectedFileId = null,
                        selectedFileContent = null,
                        activeChatId = null,
                        activeChatLink = null,
                        messages = emptyList()
                    )
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCleared() {
        super.onCleared()
        GlobalScope.launch(dispatcherProvider.io) {
            val chatActiveLink = _uiState.value.activeChatLink
            if (chatActiveLink != null) {
                leaveChatUseCase(chatActiveLink, projectId)
            }
            leaveFromProjectRoomUseCase(projectId)
        }
    }
}

/**
 * Команды.
 */
sealed interface ProjectCommand {
    data class OpenCreateFileDialog(val parentId: Int?) : ProjectCommand
    data class OpenRenameFileDialog(val fileId: Int) : ProjectCommand
    data object CloseCreateFileDialog : ProjectCommand
    data object CloseRenameFileDialog : ProjectCommand
    data class ChangeFileNameForCreate(val name: String) : ProjectCommand
    data class ChangeFileNameForRename(val name: String) : ProjectCommand
    data object ClickFileSelectedInCreatingFileDialog : ProjectCommand
    data object ClickFolderSelectedInCreatingFileDialog : ProjectCommand
    data class ClickCreateFile(val parentId: Int?) : ProjectCommand
    data class ClickDeleteFile(val fileId: Int) : ProjectCommand
    data class ClickRenameFile(val fileId: Int, val newName: String) : ProjectCommand
    data class ClickFile(val fileId: Int) : ProjectCommand
    data object ClickRunCode : ProjectCommand
    data object ClickStopCode : ProjectCommand
    data object ClickClearTerminal : ProjectCommand
    data class ChangeTerminalInput(val input: String) : ProjectCommand
    data object SendInputToTerminalFromText : ProjectCommand
    data class ClickDeleteFileFromRecentlyFiles(val fileId: Int) : ProjectCommand
    data class ClickChat(val identificator: String) : ProjectCommand
    data class ChangeChatInput(val input: String) : ProjectCommand
    data object CloseChat : ProjectCommand
    data object CreateMessage : ProjectCommand
    data object CloseTerminalPanel : ProjectCommand
    data object ChangeTerminalPanelVisible : ProjectCommand
    data class UpdateFileContent(val fileId: Int, val content: String) : ProjectCommand
}

/**
 * Состояние экрана.
 */
data class ProjectState(
    val files: List<FileNode> = emptyList(),
    val isCreateFileDialogOpen: Boolean = false,
    val isRenameFileDialogOpen: Boolean = false,
    val isTerminalPanelVisible: Boolean = false,
    val fileNameForCreate: String = "",
    val fileNameForRename: String = "",
    val isFolderSelectedInCreatingFileDialog: Boolean = false,
    val parentIdInCreatingFileDialog: Int? = null,
    val fileIdInRenamingFileDialog: Int? = null,
    val selectedFileId: Int? = null,
    val selectedFileContent: String? = null,
    val consoleLines: List<String> = emptyList(),
    val terminalInput: String = "",
    val recentlyFiles: Set<FileNode> = HashSet(),
    val activeChatLink: String? = null,
    val activeChatId: Int? = null,
    val messages: List<Message> = emptyList(),
    val chatInputValue: String = "",
    val isProgramEnded: Boolean = true
)

/**
 * События экрана проекта.
 */
sealed interface ProjectEffect {
    data class ShowMessage(val message: String) : ProjectEffect
    data object RemovedFromProject : ProjectEffect
}