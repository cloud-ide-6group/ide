package ru.vsu.front.notifications.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.Notification

/**
 * Компонент уведомления.
 *
 * @param notification Сущность, содержащая основные сведения об уведомлении.
 * @param modifier Modifier для настроек.
 * @param background Цвет бекграунда.
 * @param onDeclineClick Коллбек, вызываемый при нажатии на кнопку "Отклонить".
 */
@Composable
internal fun NotificationItem(
    notification: Notification,
    modifier: Modifier = Modifier,
    background: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.035f),
    onDeclineClick: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(background)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CodeTogetherText(
                text = notification.senderName.replaceFirstChar {
                    it.uppercaseChar()
                },
                style = CodeTogetherTheme.typography.style.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = CodeTogetherTheme.colors.primary
            )
            CodeTogetherText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = CodeTogetherTheme.colors.primaryText)
                    ) {
                        append("Invited you to the project ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = CodeTogetherTheme.colors.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    ) {
                        append(notification.projectName)
                    }
                }
            )
        }
        Spacer(Modifier.weight(1f))
        CodeTogetherTextButton(
            text = "Read",
            textColor = CodeTogetherTheme.colors.primary,
            unHoverColor = CodeTogetherTheme.colors.primary.copy(0.01f)
        ) {
            onDeclineClick(notification.notificationId)
        }
    }
}

@Composable
@Preview
private fun NotificationItemPreview() {
    BackgroundPreview {
        NotificationItem(
            notification = Notification(
                notificationId = 1,
                senderName = "Dmitry",
                sendTime = "...",
                projectId = 1,
                projectName = "Project Name"
            ),
            onDeclineClick = {

            }
        )
    }
}