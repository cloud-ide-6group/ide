package ru.vsu.front.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import front.core.designsystem.generated.resources.Res
import front.core.designsystem.generated.resources.visibility_off_24dp
import front.core.designsystem.generated.resources.visibility_on_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Кнопка с поддержкой переключения видимости.
 *
 * @param isVisible Текущее состояние видимости контента. `true` - контент виден, `false` - контент не виден.
 * @param modifier Модификатор для настройки.
 * @param onClick Коллбек, который вызывается при нажатии на кнопку для изменения видимости.
 */
@Composable
fun VisibilityButton(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconRes =
        if (isVisible) Res.drawable.visibility_off_24dp else Res.drawable.visibility_on_24dp
    CodeTogetherIconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = "Toggle confirm password visibility",
            tint = CodeTogetherTheme.colors.primary
        )
    }
}