package ru.vsu.front.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

/**
 * Composable Тема
 *
 * @param themeVariant Текущий вариант темы
 * @param content Слот под контент
 */
@Composable
fun CodeTogetherTheme(
    themeVariant: CodeTogetherThemeVariant = CodeTogetherThemeVariant.Omni,
    content: @Composable () -> Unit
) {
    val colors = when (themeVariant) {
        CodeTogetherThemeVariant.Omni -> omniPalette
    }

    val typography = CodeTogetherTypography(
        style = TextStyle(fontFamily = FontFamily.Monospace)
    )

    CompositionLocalProvider(
        LocalCodeTogetherColors provides colors,
        LocalCodeTogetherTypography provides typography,
        content = content
    )
}