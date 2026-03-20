package ru.vsu.front.features.auth.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Обёртка над Scaffold для экранов аутентификации.
 *
 * @param snackbarHostState Состояние для управления Snackbar.
 * @param modifier Модификатор корневого контейнера.
 * @param content Слот для основного контента экрана.
 */
@Composable
internal fun AuthScaffoldWrapper(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier
            .background(CodeTogetherTheme.colors.secondaryBackground)
            .padding(32.dp)
            .fillMaxSize(),
        containerColor = CodeTogetherTheme.colors.secondaryBackground,
        snackbarHost = {
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
                    color = CodeTogetherTheme.colors.error
                )
            }
        }
    ) {
        content()
    }
}