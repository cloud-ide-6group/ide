package ru.vsu.front.projects.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.Message

/**
 * Карточка сообщения
 *
 * @param message Модель данных сообщения.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun Message(
    message: Message,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CodeTogetherTheme.colors.primary.copy(alpha = 0.025f))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CodeTogetherText(
                        text = message.author,
                        style = TextStyle(color = CodeTogetherTheme.colors.primaryText, fontSize = 14.sp),
                    )
                    CodeTogetherText(
                        text = message.sendTime,
                        color = CodeTogetherTheme.colors.secondaryText,
                        style = TextStyle(color = CodeTogetherTheme.colors.secondaryText, fontSize = 13.sp),
                    )
                }
                CodeTogetherText(
                    text = message.text,
                    color = CodeTogetherTheme.colors.secondaryText,
                    style = TextStyle(fontSize = 13.sp),
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}