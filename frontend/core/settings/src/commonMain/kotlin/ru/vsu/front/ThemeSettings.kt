package ru.vsu.front

import androidx.compose.ui.graphics.Color
import ru.vsu.front.designsystem.theme.DEFAULT_PRIMARY_COLOR
import java.util.prefs.Preferences

class ThemeSettings {
    private val prefs: Preferences = Preferences.userRoot().node("ru/vsu/front/settings")

    private val defaultColorValue = DEFAULT_PRIMARY_COLOR.value.toLong()

    /**
     * Сохраняет выбранный цвет
     */
    fun savePrimaryColor(color: Color) {
        prefs.putLong(PRIMARY_COLOR_KEY, color.value.toLong())
    }

    /**
     * Загружает сохраненный цвет или возвращает стандартный
     */
    fun loadPrimaryColor(): Color {
        val savedValue = prefs.getLong(PRIMARY_COLOR_KEY, defaultColorValue)
        return Color(savedValue.toULong())
    }

    companion object {
        private const val PRIMARY_COLOR_KEY = "primary_color"
    }
}