package ru.vsu.front.profile.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import front.feature.profile.generated.resources.Res
import front.feature.profile.generated.resources.confirm_24dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Иконка подтверждения.
 *
 * @param onClick Коллбек, вызываемый при клике на кнопку.
 * @param modifier Modifier для настройки.
 * @param tint Цвет иконки.
 * @param iconRes Ресурс иконки.
 */
@Composable
internal fun ConfirmIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = CodeTogetherTheme.colors.confirm,
    iconRes: DrawableResource = Res.drawable.confirm_24dp
) {
    CodeTogetherIconButton(
        onClick = onClick,
        modifier = modifier,
        content = {
            Icon(
                painter = painterResource(resource = iconRes),
                contentDescription = "Confirm changes",
                tint = tint
            )
        },
    )
}

@Composable
@Preview
private fun ConfirmIconPreview() {
    BackgroundPreview {
        ConfirmIcon(
            onClick = {},
            modifier = Modifier
        )
    }
}