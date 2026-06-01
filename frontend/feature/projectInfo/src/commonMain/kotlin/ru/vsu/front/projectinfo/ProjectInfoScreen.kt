package ru.vsu.front.projectinfo

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.*
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.projectinfo.component.InvitingUser
import ru.vsu.front.projectinfo.component.Members
import ru.vsu.front.projectinfo.component.MembersHeader
import ru.vsu.front.projectinfo.component.ReadOnlyFiles

/**
 * Экран проекта.
 *
 * @param viewModel Вьюмодель.
 * @param onMinimizeClick Коллбек для сворачивания окна приложения.
 * @param onMaximizeClick Коллбек для разворачивания/восстановления окна приложения.
 * @param onCloseClick Коллбек для закрытия приложения.
 * @param onSettingsClick Коллбек для перехода в настройки.
 * @param onLogoutClick Коллбек для выхода из аккаунта пользователя.
 * @param onBackClick Коллбек для возврата на предыдущий экран.
 * @param onRemovedFromProject Коллбек вызывающийся когда пользователь исключен из проекта.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun WindowScope.ProjectInfoScreen(
    viewModel: ProjectInfoViewModel,
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

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ProjectInfoEffect.ProjectDeleted -> {
                    onRemovedFromProject()
                }

                is ProjectInfoEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                ProjectInfoEffect.RemovedFromCurrentProject -> {
                    onRemovedFromProject()
                }
            }
        }
    }
    CodeTogetherScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        onMinimizeClick = onMinimizeClick,
        onMaximizeClick = onMaximizeClick,
        onCloseClick = onCloseClick,
        backgroundColor = CodeTogetherTheme.colors.primaryBackground,
        topBarContent = {
            TopBarButton(
                icon = AppIcons.Settings,
                onClick = onSettingsClick,
            )
            TopBarButton(
                icon = AppIcons.Logout,
                onClick = onLogoutClick,
            )
            TopBarButton(
                icon = AppIcons.Back,
                onClick = onBackClick,
            )
            val currentState = uiState
            if (currentState is UiStatusProjectInfo.Loaded && currentState.uiStatusProjectInfo.projectInfo.isUserOwner) {
                TopBarButton(
                    icon = AppIcons.Delete,
                    onClick = {
                        viewModel.processCommand(ProjectInfoCommand.ClickDeleteProject)
                    }
                )
            }
        }
    ) {
        when (val currentState = uiState) {
            is UiStatusProjectInfo.Loaded -> {
                val loadedState = currentState.uiStatusProjectInfo
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 128.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CodeTogetherText(
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            text = loadedState.projectInfo.projectName,
                            style = TextStyle(fontSize = 32.sp),
                            color = CodeTogetherTheme.colors.primary
                        )
                        CodeTogetherText(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CodeTogetherTheme.colors.primary.copy(alpha = 0.05f))
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            text = loadedState.projectInfo.languageName,
                            color = CodeTogetherTheme.colors.primaryText
                        )
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        ReadOnlyFiles(
                            modifier = Modifier
                                .weight(1f),
                            nodes = loadedState.projectFiles
                        )
                        if (loadedState.projectInfoErrorLoading) {
                            ErrorScreen(
                                onClick = {
                                    viewModel.processCommand(ProjectInfoCommand.ClickRepeatLoadingProjectInfo)
                                }
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                MembersHeader(
                                    isOwner = loadedState.projectInfo.isUserOwner,
                                    membersAreVisible = loadedState.areMembersVisible,
                                    onInviteClick = {
                                        viewModel.processCommand(ProjectInfoCommand.ClickToggleInviteUserDialogVisible)
                                    },
                                    onToggleVisibilityClick = {
                                        viewModel.processCommand(ProjectInfoCommand.ClickToggleMembersVisibility)
                                    }
                                )
                                CodeTogetherAnimatedVisibility(
                                    visible = loadedState.areMembersVisible,
                                    enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                                ) {
                                    Members(
                                        members = loadedState.projectInfo.users,
                                        isOwner = loadedState.projectInfo.isUserOwner,
                                        onKickClick = { email ->
                                            viewModel.processCommand(ProjectInfoCommand.ClickKickMember(email))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                CustomDialog(
                    show = loadedState.isInviteUserDialogVisible,
                    onDismissRequest = {
                        viewModel.processCommand(ProjectInfoCommand.ClickCloseInviteUserDialogVisible)
                    }
                ) {
                    InvitingUser(
                        userEmail = loadedState.inputtedUserEmail,
                        onValueChanged = {
                            viewModel.processCommand(ProjectInfoCommand.ChangeInputtedUserEmail(it))
                        },
                        onClickInviteUser = {
                            viewModel.processCommand(ProjectInfoCommand.ClickInviteMember)
                        }
                    )
                }
            }

            UiStatusProjectInfo.Loading -> {
                LoadingScreen()
            }
        }
    }
}