package ru.vsu.front.projectinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.*
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.projectinfo.component.ReadOnlyFiles

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
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    CodeTogetherText(
                        modifier = Modifier.padding(start = 8.dp),
                        text = loadedState.projectInfo.projectName,
                        style = TextStyle(fontSize = 32.sp),
                        color = CodeTogetherTheme.colors.primary
                    )
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
                            ErrorScreen() {

                            }
                        } else {
                            CodeTogetherText(text = loadedState.projectInfo.users.toString())
                        }
                    }
                }
            }

            UiStatusProjectInfo.Loading -> {
                LoadingScreen()
            }
        }
    }
}