@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Code Together текст
 *
 * @param text Текст текста
 * @param modifier Modifier который будет применён к тексту
 * @param color Цвет текста
 * @param style Стиль текста
 * @param textAlign Положение текста
 * @param overflow Стратегия переполнения текста
 * @param maxLines Максимальное количество строк
 */
@Composable
fun CodeTogetherText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = CodeTogetherTheme.colors.primaryText,
    style: TextStyle = CodeTogetherTheme.typography.style,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines
    )
}

@Preview
@Composable
private fun CodeTogetherTextPreview() {
    BackgroundPreview {
        CodeTogetherText(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Hello World!",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeTogetherText(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Hello World!Hello World!Hello World!Hello World!",
            textAlign = TextAlign.Center
        )
    }
}