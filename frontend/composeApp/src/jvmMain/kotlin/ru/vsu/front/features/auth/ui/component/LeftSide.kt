@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Левая часть экранов авторизации и регистрации
 *
 * @param modifier Modifier, который будет применён к данной части
 */
@Composable
internal fun RowScope.LeftSide(
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
        Spacer(modifier = modifier.height(8.dp))
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