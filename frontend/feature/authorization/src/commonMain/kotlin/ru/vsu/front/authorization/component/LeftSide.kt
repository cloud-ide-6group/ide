package ru.vsu.front.authorization.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Левая часть экранов авторизации и регистрации.
 *
 * @param modifier Модификатор для настройки.
 */
@Composable
internal fun LeftSide(
    modifier: Modifier = Modifier,
) {
    SideColumn(modifier = modifier) {
        CodeTogetherText(
            text = "Let's Code Together!",
            style = CodeTogetherTheme.typography.style.copy(
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        CodeTogetherText(
            text = "Enjoy programming!",
            color = CodeTogetherTheme.colors.primary,
            style = CodeTogetherTheme.typography.style.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
@Preview
private fun LeftSidePreview() {
    BackgroundPreview {
        LeftSide()
    }
}