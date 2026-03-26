package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Вспомогательный контейнер для отображения `@Preview`.
 *
 * @param content Слот для UI-компонентов, которые нужно отобразить в превью.
 */
@Composable
fun BackgroundPreview(
    content: @Composable () -> Unit
) {
    CodeTogetherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeTogetherTheme.colors.primaryBackground),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun BackgroundPreviewPreview(
) {
    BackgroundPreview {

    }
}