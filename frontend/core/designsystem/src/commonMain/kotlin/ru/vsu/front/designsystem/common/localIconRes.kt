package ru.vsu.front.designsystem.common

import front.core.designsystem.generated.resources.Res
import front.core.designsystem.generated.resources.java
import front.core.designsystem.generated.resources.javascript
import front.core.designsystem.generated.resources.lua
import front.core.designsystem.generated.resources.python
import org.jetbrains.compose.resources.DrawableResource
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Расширение для получения SVG иконки по названию языка.
 */
val ProgramingLanguage.localIconRes: DrawableResource?
    get() {
        return when {
            name.contains("python", ignoreCase = true) -> Res.drawable.python
            name.contains("javascript", ignoreCase = true) -> Res.drawable.javascript
            name.contains("java", ignoreCase = true) -> Res.drawable.java
            name.contains("lua", ignoreCase = true) -> Res.drawable.lua
            else -> null
        }
    }