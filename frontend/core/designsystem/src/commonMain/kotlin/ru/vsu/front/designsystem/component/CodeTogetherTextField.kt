package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
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
 * Поле ввода текста приложения.
 *
 * @param value Текущий введенный текст.
 * @param onValueChange Коллбек, вызываемый при каждом изменении текста.
 * @param modifier Модификатор для настройки.
 * @param textColor Цвет текста.
 * @param colors Цветовая схема текста.
 * @param hint Текст подсказки.
 * @param trailingIcon Слот для иконки в конце поля.
 * @param visualTransformation Визуальная трансформация текста.
 * @param hintColor Цвет текста подсказки.
 * @param style Стиль текста.
 * @param singleLine Ограничивает поле ввода одной строкой.
 * @param isError Флаг состояния ошибки. Если `true`, поле подсвечивается красным.
 */
@Composable
fun CodeTogetherTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = CodeTogetherTheme.colors.primaryText,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    hint: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    hintColor: Color = CodeTogetherTheme.colors.secondaryText,
    style: TextStyle = CodeTogetherTheme.typography.style,
    singleLine: Boolean = true,
    isError: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = style,
        singleLine = singleLine,
        isError = isError,
        placeholder = {
            CodeTogetherText(
                text = hint,
                color = hintColor,
                style = CodeTogetherTheme.typography.style
            )
        },
        colors = colors.copy(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedIndicatorColor = CodeTogetherTheme.colors.primary,
            errorTextColor = CodeTogetherTheme.colors.error,
            errorIndicatorColor = CodeTogetherTheme.colors.error,
            errorCursorColor = textColor,
            textSelectionColors = CodeTogetherTheme.selectionColors,
            cursorColor = CodeTogetherTheme.colors.primary
        ),
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation
    )
}

@Preview
@Composable
private fun CodeTogetherTextFieldPreview() {
    BackgroundPreview {
        CodeTogetherTextField(
            value = "Current value!!!!!!!!! world",
            onValueChange = {

            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeTogetherTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = "Very very very very very very very very very long word",
            onValueChange = {

            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        CodeTogetherTextField(
            modifier = Modifier,
            value = "",
            hint = "I am long long long long Hint",
            onValueChange = {

            }
        )
    }
}