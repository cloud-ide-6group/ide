package ru.vsu.front.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Корневой Composable-компонент темы приложения.
 *
 * @param themeVariant Выбранный вариант темы.
 * @param content Composable-элементы, к которым будет применена тема.
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
        style = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        )
    )

    CompositionLocalProvider(
        LocalCodeTogetherColors provides colors,
        LocalCodeTogetherTypography provides typography,
        content = content
    )
}