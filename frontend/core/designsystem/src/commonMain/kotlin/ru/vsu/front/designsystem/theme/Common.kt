package ru.vsu.front.designsystem.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import ru.vsu.front.designsystem.theme.CodeTogetherTheme.colors
import ru.vsu.front.designsystem.theme.CodeTogetherTheme.typography

/**
 * Набор цветов приложения.
 *
 * @property primary Основной цвет.
 * @property primaryText Основной цвет текста.
 * @property primaryBackground Главный цвет фона приложения.
 * @property secondaryText Вторичный цвет текста.
 * @property secondaryBackground Вторичный цвет фона.
 * @property error Цвет ошибок.
 * @property black Чёрный цвет.
 */
data class CodeTogetherColors(
    val primary: Color,
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val error: Color,
    val confirm: Color,
    val black: Color
)

/**
 * Типографика дизайн-системы.
 * @property style Базовый стиль текста приложения.
 */
data class CodeTogetherTypography(
    val style: TextStyle
)

/**
 * Глобальный объект для доступа к текущей теме приложения.
 * Позволяет получать цвета [colors] и типографику [typography] в любом Composable.
 */
object CodeTogetherTheme {
    val colors: CodeTogetherColors
        @Composable
        get() = LocalCodeTogetherColors.current

    val typography: CodeTogetherTypography
        @Composable
        get() = LocalCodeTogetherTypography.current

    val selectionColors: TextSelectionColors
        @Composable
        get() = LocalCodeTogetherSelectionColors.current
}

/**
 * Доступные варианты темы.
 */
enum class CodeTogetherThemeVariant {
    Omni,
}

/**
 * Провайдер текущей цветовой палитры.
 */
val LocalCodeTogetherColors = staticCompositionLocalOf<CodeTogetherColors> {
    error("Цвета не предоставлены")
}

/**
 * Провайдер текущей типографики.
 */
val LocalCodeTogetherTypography = staticCompositionLocalOf<CodeTogetherTypography> {
    error("Стиль не предоставлен")
}

/**
 * Провайдер текущих цветов выделения текста.
 */
val LocalCodeTogetherSelectionColors = staticCompositionLocalOf<TextSelectionColors> {
    error("Цвета не предоставлены")
}