package ru.vsu.front.projects

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.*
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.projects.component.*
import ru.vsu.front.projects.component.editor.CodeLanguage
import ru.vsu.front.projects.component.editor.TextFieldCodeEditor

/**
 * Главный экран рабочего пространства проекта, объединяющий дерево файлов, редактор кода и терминал.
 *
 * @param viewModel Модель представления для управления состоянием проекта (файлы, код, сокеты).
 * @param onMinimizeClick Коллбек для сворачивания окна приложения.
 * @param onMaximizeClick Коллбек для разворачивания/восстановления окна приложения.
 * @param onCloseClick Коллбек для закрытия приложения.
 * @param onSettingsClick Коллбек для перехода в настройки.
 * @param onLogoutClick Коллбек для выхода из аккаунта пользователя.
 * @param onBackClick Коллбек для возврата на предыдущий экран.
 * @param modifier Модификатор для настройки макета компонента.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WindowScope.ProjectScreen(
    viewModel: ProjectViewModel,
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    onRemovedFromProject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ProjectEffect.ShowMessage -> snackbarHostState.showSnackbar(message = event.message)
                ProjectEffect.RemovedFromProject -> {
                    onRemovedFromProject()
                }
            }
        }
    }

    CodeTogetherScaffold(
        modifier = modifier,
        backgroundColor = CodeTogetherTheme.colors.primaryBackground,
        snackbarHostState = snackbarHostState,
        onMinimizeClick = onMinimizeClick,
        onMaximizeClick = onMaximizeClick,
        onCloseClick = onCloseClick,
        topBarContent = {
            TopBarButton(
                onClick = onSettingsClick,
                icon = AppIcons.Settings
            )
            TopBarButton(
                onClick = onLogoutClick,
                icon = AppIcons.Logout
            )
            TopBarButton(
                onClick = onBackClick,
                icon = AppIcons.Back
            )
            TopBarButton(
                onClick = {
                    viewModel.processCommand(ProjectCommand.ClickRunCode)
                },
                icon = AppIcons.Run
            )
            TopBarButton(
                onClick = {
                    viewModel.processCommand(ProjectCommand.ClickStopCode)
                },
                icon = AppIcons.Stop
            )
            TopBarButton(
                onClick = {
                    viewModel.processCommand(ProjectCommand.ChangeTerminalPanelVisible)
                },
                text = "Terminal",
                unHoverColor = if (uiState.isTerminalPanelVisible) CodeTogetherTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            var isMenuExpanded by remember { mutableStateOf(false) }
            var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
            val density = LocalDensity.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row {
                    Files(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent()
                                        if (event.type == PointerEventType.Press
                                            && event.buttons.isSecondaryPressed
                                            && event.changes.any { !it.isConsumed }
                                        ) {
                                            val position = event.changes.first().position
                                            menuOffset = with(density) {
                                                DpOffset(position.x.toDp(), position.y.toDp())
                                            }
                                            isMenuExpanded = true
                                        }
                                    }
                                }
                            },
                        nodes = uiState.files,
                        onFileClick = { fileId ->
                            viewModel.processCommand(ProjectCommand.ClickFile(fileId))
                        },
                        onDeleteClick = { fileId ->
                            viewModel.processCommand(ProjectCommand.ClickDeleteFile(fileId))
                        },
                        onCreateFileClick = { fileId ->
                            viewModel.processCommand(ProjectCommand.OpenCreateFileDialog(fileId))
                        },
                        onRenameFileClick = { fileId ->
                            viewModel.processCommand(ProjectCommand.OpenRenameFileDialog(fileId))
                        }
                    )
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        offset = menuOffset,
                        containerColor = CodeTogetherTheme.colors.secondaryBackground
                    ) {
                        DropdownMenuItem(
                            text = { CodeTogetherText("Create file") },
                            onClick = {
                                isMenuExpanded = false
                                viewModel.processCommand(ProjectCommand.OpenCreateFileDialog(null))
                            }
                        )
                    }

                    val selectedFileId = uiState.selectedFileId
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (selectedFileId != null) CodeTogetherTheme.colors.primaryBackground else CodeTogetherTheme.colors.secondaryBackground),
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        if (selectedFileId != null) {
                            RecentlyFiles(
                                files = uiState.recentlyFiles.toList(),
                                selectedFileId = uiState.selectedFileId,
                                onClick = {
                                    viewModel.processCommand(ProjectCommand.ClickFile(it))
                                },
                                onCloseClick = {
                                    viewModel.processCommand(ProjectCommand.ClickDeleteFileFromRecentlyFiles(it))
                                }
                            )

                            val selectedFileName = remember(selectedFileId, uiState.recentlyFiles) {
                                uiState.recentlyFiles.find { it.id == selectedFileId }?.name ?: ""
                            }

                            val currentLanguage = remember(selectedFileName) {
                                CodeLanguage.fromFileName(selectedFileName)
                            }

                            key(selectedFileId) {
                                TextFieldCodeEditor(
                                    modifier = Modifier
                                        .weight(1f),
                                    text = uiState.selectedFileContent ?: "",
                                    language = currentLanguage, // 3. ПЕРЕДАЕМ ВЫЧИСЛЕННЫЙ ЯЗЫК
                                    onContentChanged = { content ->
                                        viewModel.processCommand(
                                            ProjectCommand.UpdateFileContent(
                                                fileId = selectedFileId,
                                                content = content
                                            )
                                        )
                                    },
                                    onLinkClick = { identificator ->
                                        viewModel.processCommand(ProjectCommand.ClickChat(identificator))
                                    }
                                )
                            }
                        }

                        CodeTogetherAnimatedVisibility(
                            visible = uiState.isTerminalPanelVisible,
                            enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                            exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
                        ) {
                            TerminalPanel(
                                lines = uiState.consoleLines,
                                consoleInput = uiState.terminalInput,
                                onInputChange = {
                                    viewModel.processCommand(ProjectCommand.ChangeTerminalInput(it))
                                },
                                onEnterPressed = {
                                    viewModel.processCommand(ProjectCommand.SendInputToTerminalFromText)
                                },
                                onCloseClick = {
                                    viewModel.processCommand(ProjectCommand.CloseTerminalPanel)
                                },
                                onClearClick = {
                                    viewModel.processCommand(ProjectCommand.ClickClearTerminal)
                                }
                            )
                        }
                    }

                    CodeTogetherAnimatedVisibility(
                        visible = uiState.activeChatLink != null
                    ) {
                        Messages(
                            messages = uiState.messages,
                            link = uiState.activeChatLink ?: "",
                            inputValue = uiState.chatInputValue,
                            onInputChange = {
                                viewModel.processCommand(ProjectCommand.ChangeChatInput(it))
                            },
                            onSendClick = {
                                viewModel.processCommand(ProjectCommand.CreateMessage)
                            },
                            onCloseClick = {
                                viewModel.processCommand(ProjectCommand.CloseChat)
                            }
                        )
                    }
                }
            }
        }

        CustomDialog(
            show = uiState.isCreateFileDialogOpen,
            onDismissRequest = {
                viewModel.processCommand(ProjectCommand.CloseCreateFileDialog)
            },
            content = {
                CreatingFile(
                    fileName = uiState.fileNameForCreate,
                    parentId = uiState.parentIdInCreatingFileDialog,
                    onCreateClick = {
                        viewModel.processCommand(
                            ProjectCommand.ClickCreateFile(
                                parentId = it
                            )
                        )
                    },
                    onValueChange = {
                        viewModel.processCommand(ProjectCommand.ChangeFileNameForCreate(it))
                    },
                    isFolderSelected = uiState.isFolderSelectedInCreatingFileDialog,
                    onClickSelectFile = {
                        viewModel.processCommand(ProjectCommand.ClickFileSelectedInCreatingFileDialog)
                    },
                    onClickSelectFolder = {
                        viewModel.processCommand(ProjectCommand.ClickFolderSelectedInCreatingFileDialog)
                    },
                )
            }
        )

        CustomDialog(
            show = uiState.isRenameFileDialogOpen,
            onDismissRequest = {
                viewModel.processCommand(ProjectCommand.CloseRenameFileDialog)
            },
            content = {
                RenamingFile(
                    fileName = uiState.fileNameForRename,
                    fileId = uiState.fileIdInRenamingFileDialog!!,
                    onValueChange = {
                        viewModel.processCommand(ProjectCommand.ChangeFileNameForRename(it))
                    },
                    onRenameClick = { fileId, fileName ->
                        viewModel.processCommand(ProjectCommand.ClickRenameFile(fileId, fileName))
                    },
                )
            }
        )
    }
}