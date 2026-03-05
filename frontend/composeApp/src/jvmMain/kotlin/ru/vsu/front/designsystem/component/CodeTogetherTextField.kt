package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

@Composable
fun CodeTogetherTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = CodeTogetherTheme.colors.primaryText,
    hint: String = "",
    hintColor: Color = CodeTogetherTheme.colors.secondaryText,
    style: TextStyle = CodeTogetherTheme.typography.style,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = style,
        singleLine = singleLine,
        placeholder = {
            CodeTogetherText(
                text = hint,
                color = hintColor
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        )
    )
}

@Preview
@Composable
fun CodeTogetherTextField() {
    CodeTogetherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeTogetherTheme.colors.primaryBackground),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CodeTogetherTextField(
                value = "Current value!!!!!!!!! world",
                onValueChange = {

                }
            )
            CodeTogetherTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "Very very very very very very very very very long word",
                onValueChange = {

                }
            )
            CodeTogetherTextField(
                modifier = Modifier,
                value = "",
                hint = "I am long long long long Hint",
                onValueChange = {

                }
            )
        }
    }
}