package ru.vsu.front.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Представляет собой не дефолтную кнопку-иконку в верхней панели приложения.
 *
 * @param modifier Modifier для настройки.
 * @param onClick Коллбек, вызываемый при клике на кнопку.
 * @param icon Иконка кнопки.
 */
@Composable
internal fun TopBarButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: DrawableResource
) {
    CodeTogetherIconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Logout",
            tint = CodeTogetherTheme.colors.primary,
        )
    }
}