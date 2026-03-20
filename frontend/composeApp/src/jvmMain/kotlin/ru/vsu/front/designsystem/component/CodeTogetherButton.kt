package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import front.composeapp.generated.resources.Res
import front.composeapp.generated.resources.close_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Базовая кнопка приложения.
 *
 * @param onClick Коллбек, вызываемый при клике на кнопку.
 * @param modifier Модификатор для настройки.
 * @param padding Внутренние отступы контента.
 * @param shape Форма кнопки.
 * @param hoverColor Цвет фона при наведении курсора мыши.
 * @param unhoverColor Цвет фона в обычном состоянии.
 * @param content Слот для внутреннего контента кнопки.
 */
@Composable
fun CodeTogetherButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(),
    shape: Shape = RoundedCornerShape(8.dp),
    hoverColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
    unhoverColor: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .clip(shape)
            .background(if (isHovered) hoverColor else unhoverColor)
            .clickable(
                interactionSource = interactionSource,
                onClick = onClick
            )
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview
@Composable
private fun CodeTogetherButtonPreview() {
    BackgroundPreview {
        CodeTogetherButton(
            onClick = {
            },
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24dp),
                contentDescription = "",
                tint = CodeTogetherTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        CodeTogetherButton(
            onClick = {
            },
            unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24dp),
                contentDescription = "",
                tint = CodeTogetherTheme.colors.error
            )
        }
    }
}