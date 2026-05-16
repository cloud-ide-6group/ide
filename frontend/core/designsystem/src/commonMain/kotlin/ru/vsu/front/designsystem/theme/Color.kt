package ru.vsu.front.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Стандартный первичный цвет.
 */
val DEFAULT_PRIMARY_COLOR = Color(0xFFFF79C6)

/**
 * Цветовая палитра приложения.
 */
val omniPalette = CodeTogetherColors(
    primary = DEFAULT_PRIMARY_COLOR,
    primaryText = Color(0xFFE1E1E6),
    primaryBackground = Color(0xFF13111B),
    secondaryText = Color(0x80888888),
    secondaryBackground = Color(0xFF191622),
    error = Color(0xFFFF5555),
    confirm = Color(0xFF22FF0B),
    black = Color(0xFF000000)
)