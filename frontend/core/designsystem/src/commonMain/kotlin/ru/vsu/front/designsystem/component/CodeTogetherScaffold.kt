package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Базовый Scaffold приложения.
 *
 * @param modifier Модификатор корневого контейнера.
 * @param backgroundColor Цвет бекграунда.
 * @param snackbarHostState Состояние для управления Snackbar.
 * @param content Слот для контента.
 */
@Composable
fun CodeTogetherScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = CodeTogetherTheme.colors.secondaryBackground,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                }
            ),
        containerColor = backgroundColor,
        snackbarHost = {
            snackbarHostState?.let { state ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    SnackbarHost(
                        hostState = state,
                        modifier = Modifier
                            .padding(16.dp)
                            .shadow(elevation = 8.dp, shape = CircleShape)
                            .clip(CircleShape),
                    ) {
                        CodeTogetherText(
                            modifier = Modifier
                                .background(linearGradient(listOf(
                                    CodeTogetherTheme.colors.secondaryBackground,
                                    CodeTogetherTheme.colors.primaryBackground,
                                )))
                                .padding(24.dp),
                            text = it.visuals.message,
                            style = CodeTogetherTheme.typography.style.copy(fontSize = 18.sp),
                            textAlign = TextAlign.Center,
                            color = CodeTogetherTheme.colors.primary
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            content()
        }
    }
}