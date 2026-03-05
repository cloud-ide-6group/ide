@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
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
 * Code Together кнопка
 *
 * @param onClick Коллбек, вызывающийся при клике на кнопку
 * @param modifier Modifier который будет применён к кнопке
 * @param shape Фигура, которая будет применена к кнопке
 * @param hoverColor Цвет бекграунда, когда курсор на кнопке
 * @param content Слот под контент
 */
@Composable
fun CodeTogetherButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    hoverColor: Color = Color.White.copy(alpha = 0.10f),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(shape)
            .background(if (isHovered) hoverColor else Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview
@Composable
private fun CodeTogetherButtonPreview() {
    BackgroundPreview {
        IconButton(
            onClick = {

            },
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24dp),
                contentDescription = "",
                tint = CodeTogetherTheme.colors.error
            )
        }

        IconButton(
            onClick = {

            },
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.close_24dp),
                contentDescription = "",
                tint = CodeTogetherTheme.colors.primary
            )
        }
    }
}