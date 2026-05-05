package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
/**
 * Экран загрузки.
 *
 * @param modifier Модификатор для настройки.
 * @param indicatorColor Цвет прогресс бара.
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    indicatorColor: Color = CodeTogetherTheme.colors.primary
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = indicatorColor)
    }
}