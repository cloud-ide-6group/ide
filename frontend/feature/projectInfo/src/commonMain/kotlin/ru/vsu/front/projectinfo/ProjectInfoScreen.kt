package ru.vsu.front.projectinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherScaffold
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.TopBarButton

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
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    CodeTogetherScaffold(
        modifier = modifier,
        onMinimizeClick = onMinimizeClick,
        onMaximizeClick = onMaximizeClick,
        onCloseClick = onCloseClick,
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
        }
    ) {
        CodeTogetherText(text = "Project Info Screen ${uiState.value}")
    }
}