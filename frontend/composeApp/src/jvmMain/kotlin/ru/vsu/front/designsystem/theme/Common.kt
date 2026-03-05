@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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

/**
 * Тема
 *
 * @param colors Цвета текущей темы приложения
 */
object CodeTogetherTheme {
    val colors: CodeTogetherColors
        @Composable
        get() = LocalCodeTogetherColors.current
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