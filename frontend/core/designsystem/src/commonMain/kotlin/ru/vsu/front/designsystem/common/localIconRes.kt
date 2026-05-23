package ru.vsu.front.designsystem.common

import org.jetbrains.compose.resources.DrawableResource
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Расширение для получения SVG иконки по названию языка.
 */
val ProgramingLanguage.localIconRes: DrawableResource?
    get() {
        return when {
            name.contains("python", ignoreCase = true) -> AppIcons.Python
            name.contains("javascript", ignoreCase = true) -> AppIcons.Javascript
            name.contains("java", ignoreCase = true) -> AppIcons.Java
            name.contains("lua", ignoreCase = true) -> AppIcons.Lua
            else -> null
        }
    }