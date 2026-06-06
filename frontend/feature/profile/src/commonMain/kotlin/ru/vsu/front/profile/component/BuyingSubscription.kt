package ru.vsu.front.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Карточка покупки подписки.
 *
 * @param modifier Modifier для настройки.
 * @param onConfirmClick Коллбек нажатия на кнопку "Confirm".
 * @param onDismissRequest Коллбек закрытия диалога.
 */
@Composable
internal fun BuyingSubscription(
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CodeTogetherTheme.colors.secondaryBackground,
        tonalElevation = 8.dp,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    ) {
        Column(
            modifier = Modifier
                .width(640.dp)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CodeTogetherText(
                text = "Вы уверены, что хотите оформить подписку?",
                color = CodeTogetherTheme.colors.primary,
                style = CodeTogetherTheme.typography.style.copy(fontSize = 20.sp)
            )
            CodeTogetherTextButton(
                text = "Confirm",
                modifier = Modifier.fillMaxWidth(),
                textColor = CodeTogetherTheme.colors.primary
            ) {
                onConfirmClick()
            }
        }
    }
}