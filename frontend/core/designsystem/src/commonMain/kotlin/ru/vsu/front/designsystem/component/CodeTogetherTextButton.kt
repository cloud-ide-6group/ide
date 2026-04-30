package ru.vsu.front.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Базовая текстовая кнопка приложения.
 *
 * @param text Строка, которая будет отображаться внутри кнопки.
 * @param modifier Модификатор для настройки.
 * @param shape Форма области клика.
 * @param enabled Доступность клика.
 * @param style Стиль текста.
 * @param textColor Цвет текста.
 * @param contentColor Цвет контента.
 * @param hoverColor Цвет контейнера когда курсор находится на кнопке.
 * @param unHoverColor Цвет контейнера когда курсор не находится на кнопке.
 * @param onClick Коллбек, вызываемый при клике на кнопку.
 */
@Composable
fun CodeTogetherTextButton(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    enabled: Boolean = true,
    style: TextStyle = CodeTogetherTheme.typography.style,
    textColor: Color = CodeTogetherTheme.colors.primaryText,
    contentColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.15f),
    hoverColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.05f),
    unHoverColor: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            containerColor = if (isHovered) hoverColor else unHoverColor
        )
    ) {
        CodeTogetherText(
            text = text,
            color = textColor,
            style = style
        )
    }
}

@Preview
@Composable
private fun CodeTogetherTextButtonPreview() {
    BackgroundPreview {
        CodeTogetherTextButton(
            text = "Log In",
            onClick = {
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeTogetherTextButton(
            text = "Sign Up",
            textColor = CodeTogetherTheme.colors.primary,
            style = CodeTogetherTheme.typography.style.copy(
                fontWeight = FontWeight.Bold,
            ),
            onClick = {
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeTogetherTextButton(
            text = "Sign Up",
            textColor = CodeTogetherTheme.colors.primary,
            style = CodeTogetherTheme.typography.style.copy(
                fontWeight = FontWeight.Bold,
            ),
            onClick = {
            }
        )
    }
}