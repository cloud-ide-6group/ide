package ru.vsu.front.settings

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.vsu.front.ThemeSettings

class Settings(
    private val themeSettings: ThemeSettings
) {
    private val _primaryColor = MutableStateFlow(themeSettings.loadPrimaryColor())
    val primaryColor = _primaryColor.asStateFlow()

    fun savePrimaryColor(color: Color) {
        themeSettings.savePrimaryColor(color)
        _primaryColor.update {
            color
        }
    }
}