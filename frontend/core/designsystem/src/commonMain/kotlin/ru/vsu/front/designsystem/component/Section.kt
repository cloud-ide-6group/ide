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
 * Компонент, представляющий собой связь [CodeTogetherText] и [CodeTogetherTextField] в столбик.
 *
 * @param sectionName Текст заголовка над полем ввода.
 * @param value Текущий введенный текст в поле.
 * @param hint Текст подсказки.
 * @param trailingIcon Слот для иконки в конце поля ввода.
 * @param visualTransformation Визуальная трансформация текста.
 * @param onValueChange Коллбек, вызываемый при вводе текста.
 * @param modifier Модификатор, применяемый ко всей секции.
 * @param textColor Цвет текста в поле ввода.
 * @param sectionNameTextColor Цвет текста названия секции.
 * @param textFieldColors Цветовая схема введенного текста.
 * @param style Единый стиль текста.
 * @param isError Ошибка ли. Если `true`, поле ввода переходит в состояние ошибки.
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
    sectionNameTextColor: Color = CodeTogetherTheme.colors.primary,
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
            color = sectionNameTextColor,
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
