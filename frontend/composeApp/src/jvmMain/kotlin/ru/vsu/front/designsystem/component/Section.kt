package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

@Composable
fun Section(
    sectionName: String,
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = CodeTogetherTheme.colors.primaryText,
    textFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    style: TextStyle = CodeTogetherTheme.typography.style
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
            value = "example.examp...|",
            onValueChange = {

            },
            textFieldColors = OutlinedTextFieldDefaults
                .colors(unfocusedBorderColor = CodeTogetherTheme.colors.primary),
            hint = "Your Email"
        )
    }
}
