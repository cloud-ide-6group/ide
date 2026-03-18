package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Секция в виде столбца с Text + TextField
 *
 * @param sectionName Текст для Text
 * @param value Текст для TextField
 * @param hint Текст подсказки
 * @param trailingIcon Контент, который будет применён к концу TextField
 * @param visualTransformation Визуальная трансформация текста в TextField
 * @param onValueChange Коллбек, вызывающийся при изменении value
 * @param modifier Modifier, который будет применён к секции
 * @param textColor Цвет текста для Text и TextField
 * @param textFieldColors Цвета текста для TextField
 * @param style Стиль для Text
 */
@Composable
fun Section(
    sectionName: String,
    value: String,
    hint: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = CodeTogetherTheme.colors.primaryText,
    textFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    style: TextStyle = CodeTogetherTheme.typography.style,
    isError: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        CodeTogetherText(
            modifier = Modifier
                .padding(bottom = 8.dp),
            text = sectionName,
            color = textColor,
            style = style
        )
        CodeTogetherTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            textColor = textColor,
            colors = textFieldColors,
            hint = hint,
            isError = isError,
            style = style,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            onValueChange = onValueChange
        )
    }
}

@Preview
@Composable
private fun SectionPreview() {
    BackgroundPreview {
        Section(
            sectionName = "Email",
            value = "example.example@mail.ru",
            onValueChange = {

            },
            hint = "Your Email"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Section(
            sectionName = "Email",
            value = "",
            onValueChange = {

            },
            hint = "Your Email"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Section(
            sectionName = "Email",
            value = "example.examp...",
            onValueChange = {

            },
            textFieldColors = OutlinedTextFieldDefaults
                .colors(unfocusedBorderColor = CodeTogetherTheme.colors.primary),
            hint = "Your Email"
        )
    }
}
