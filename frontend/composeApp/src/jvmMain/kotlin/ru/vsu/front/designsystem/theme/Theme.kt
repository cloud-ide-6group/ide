package ru.vsu.front.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

@Composable
fun CodeTogetherTheme(
    themeVariant: CodeTogetherThemeVariant,
    content: @Composable () -> Unit
) {
    val colors = when(themeVariant) {
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