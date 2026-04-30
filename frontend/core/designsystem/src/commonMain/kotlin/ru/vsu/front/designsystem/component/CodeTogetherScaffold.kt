package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            .fillMaxSize(),
        containerColor = backgroundColor,
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(
                    hostState = snackbarHostState
                ) {
                    CodeTogetherText(
                        modifier = Modifier
                            .background(CodeTogetherTheme.colors.black.copy(alpha = 0.1f))
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = it.visuals.message,
                        style = CodeTogetherTheme.typography.style.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                        color = CodeTogetherTheme.colors.primary
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            content()
        }
    }
}