package ru.vsu.front.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun CodeTogetherTheme(
    themeVariant: CodeTogetherThemeVariant,
    content: @Composable () -> Unit
) {
    val colors = when(themeVariant) {
        CodeTogetherThemeVariant.Omni -> omniPalette
    }

    CompositionLocalProvider(
        LocalCodeTogetherColors provides colors,
        content = content
    )
}