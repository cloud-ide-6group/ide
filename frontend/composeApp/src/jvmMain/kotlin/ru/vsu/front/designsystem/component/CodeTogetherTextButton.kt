package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Code Together  кнопка-текст
 *
 * @param text Текст кнопки
 * @param modifier Modifier который будет применён к кнопке
 * @param shape Фигура, которая будет применена к кнопке
 * @param style Стиль текста
 * @param colors Цвета кнопки
 * @param textColor Цвет текста
 * @param onClick Коллбек, вызывающийся при клике на кнопку
 */
@Composable
fun CodeTogetherTextButton(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    style: TextStyle = CodeTogetherTheme.typography.style,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    textColor: Color = CodeTogetherTheme.colors.primaryText,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = colors
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
            colors = ButtonDefaults.textButtonColors(containerColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f)),
            onClick = {
            }
        )
    }
}