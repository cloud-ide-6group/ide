package ru.vsu.front.notifications.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import front.feature.notifications.generated.resources.Res
import front.feature.notifications.generated.resources.close_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
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
fun NotificationItem(
    notification: Notification,
    modifier: Modifier = Modifier,
    background: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.035f),
    onDeclineClick: (Int) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CodeTogetherText(
                text = "${notification.senderName} пригласил Вас в проект ${notification.projectName}",
                color = CodeTogetherTheme.colors.primaryText
            )

            Spacer(Modifier.weight(1f))

            CodeTogetherIconButton(
                onClick = {
                    onDeclineClick(notification.projectId)
                }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close_24dp),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}