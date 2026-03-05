package ru.vsu.front

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowScope
import ru.vsu.front.designsystem.component.WindowTopBar
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.designsystem.theme.CodeTogetherThemeVariant

@Composable
fun WindowScope.App(
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    CodeTogetherTheme(
        themeVariant = CodeTogetherThemeVariant.Omni
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            WindowTopBar(
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CodeTogetherTheme.colors.primaryBackground)
            ) {
                content()
            }
        }
    }
}