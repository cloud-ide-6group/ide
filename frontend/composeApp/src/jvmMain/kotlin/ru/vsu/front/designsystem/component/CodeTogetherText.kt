package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

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
    CodeTogetherTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeTogetherTheme.colors.primaryBackground),
            contentAlignment = Alignment.Center,
        ) {
            CodeTogetherText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Hello World!",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun CodeTogetherTextPreview2() {
    CodeTogetherTheme {
        CodeTogetherText(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Hello World!",
            textAlign = TextAlign.Center
        )
    }
}