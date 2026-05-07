package ru.vsu.front.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import front.core.designsystem.generated.resources.Res
import front.core.designsystem.generated.resources.close_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import kotlin.math.cos

/**
 * Базовая кнопка приложения с иконкой.
 *
 * @param onClick Коллбек, который вызывается при нажатии на кнопку.
 * @param modifier Модификатор для настройки.
 * @param shape Форма кнопки.
 * @param hoverColor Цвет фона, который появляется при наведении курсора на кнопку.
 * @param unhoverColor Цвет фона, когда курсор не на кнопке.
 * @param content Слот под контент.
 */
@Composable
fun CodeTogetherIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    hoverColor: Color = Color.White.copy(alpha = 0.05f),
    unhoverColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    IconButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isHovered) hoverColor else unhoverColor
        )
    ) {
        content()
    }
}

@Composable
@Preview
fun CodeTogetherIconButtonPreview() {
    BackgroundPreview {
        CodeTogetherIconButton(
            onClick = {
            },
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24dp),
                contentDescription = null,
                tint = CodeTogetherTheme.colors.primary
            )
        }

        CodeTogetherIconButton(
            onClick = {
            },
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
            unhoverColor = Color.White.copy(alpha = 0.05f),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24dp),
                contentDescription = null,
                tint = CodeTogetherTheme.colors.primary
            )
        }
    }
}