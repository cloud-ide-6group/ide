package ru.vsu.front.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Представляет собой не кнопку-иконку в верхней панели приложения.
 *
 * @param icon Иконка кнопки.
 * @param modifier Modifier для настройки.
 * @param onClick Коллбек, вызываемый при клике на кнопку.
 */
@Composable
fun TopBarButton(
    icon: DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CodeTogetherIconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "Logout",
            tint = CodeTogetherTheme.colors.primary,
        )
    }
}

/**
 * Представляет собой текстовую кнопку для использования в верхней панели приложения.
 *
 * @param text Текст, отображаемый на кнопке.
 * @param modifier Modifier для настройки.
 * @param textColor Цвет текста кнопки.
 * @param unHoverColor Цвет когда курсор не на элементе.
 * @param onClick Коллбек, срабатывающий при нажатии на кнопку.
 */
@Composable
fun TopBarButton(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = CodeTogetherTheme.colors.primary,
    unHoverColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
    onClick: () -> Unit,
) {
    CodeTogetherTextButton(
        modifier = modifier,
        text = text,
        onClick = onClick,
        textColor = textColor,
        unHoverColor = unHoverColor
    )
}