package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import front.composeapp.generated.resources.Res
import front.composeapp.generated.resources.close_24dp
import front.composeapp.generated.resources.maximize_24dp
import front.composeapp.generated.resources.minimize_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

@Composable
fun WindowScope.WindowTopBar(
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    content: @Composable RowScope.() -> Unit = {} 
) {
    WindowDraggableArea(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(CodeTogetherTheme.colors.primaryBackground)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CodeTogetherButton(onClick = onMinimizeClick) {
                    Icon(
                        painter = painterResource(Res.drawable.minimize_24dp),
                        contentDescription = "Minimize",
                        tint = CodeTogetherTheme.colors.primary,
                    )
                }
                CodeTogetherButton(onClick = onMaximizeClick) {
                    Icon(
                        painter = painterResource(Res.drawable.maximize_24dp),
                        contentDescription = "Maximize",
                        tint = CodeTogetherTheme.colors.primary
                    )
                }
                CodeTogetherButton(onClick = onCloseClick) {
                    Icon(
                        painter = painterResource(Res.drawable.close_24dp),
                        contentDescription = "Close",
                        tint = CodeTogetherTheme.colors.primary
                    )
                }
            }
        }
    }
}