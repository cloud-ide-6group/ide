@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import ru.vsu.front.designsystem.theme.CodeTogetherTheme.colors

/**
 * Цвета [В текущей реализации цвета только для экрана логина]
 *
 * @param primary Первичный цвет темы
 * @param primaryText Первичный цвет текста
 * @param primaryBackground Первичный цвет бекграунда
 * @param secondaryText Вторичный цвет текста
 * @param secondaryBackground Вторичный цвет бекграунда
 * @param error Цвет ошибки
 */
data class CodeTogetherColors(
    val primary: Color,
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val error: Color
)

data class CodeTogetherTypography(
    val style: TextStyle
)
/**
 * Тема
 *
 * @param colors Цвета текущей темы приложения
 */
object CodeTogetherTheme {
    val colors: CodeTogetherColors
        @Composable
        get() = LocalCodeTogetherColors.current

    val typography: CodeTogetherTypography
        @Composable
        get() = LocalCodeTogetherTypography.current
}

/**
 * Темы приложения
 */
enum class CodeTogetherThemeVariant {
    Omni,
}

val LocalCodeTogetherColors = staticCompositionLocalOf<CodeTogetherColors> {
    error("Цвета не предоставлены")
}

val LocalCodeTogetherTypography = staticCompositionLocalOf<CodeTogetherTypography> {
    error("Стиль не предоставлен")
}