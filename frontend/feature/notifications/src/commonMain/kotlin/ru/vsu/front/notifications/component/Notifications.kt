package ru.vsu.front.notifications.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.designsystem.component.CodeTogetherAnimatedVerticalScrollBar
import ru.vsu.front.model.entity.Notification

/**
 * Компонент уведомлений.
 *
 * @param notifications Список уведомлений.
 * @param state Состояние для [LazyColumn].
 * @param modifier Цвет бекграунда.
 * @param onDeclineClick Коллбек, вызываемый при нажатии на кнопку "Отклонить".
 */
@Composable
internal fun Notifications(
    notifications: List<Notification>,
    state: LazyListState,
    modifier: Modifier = Modifier,
    onDeclineClick: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = state,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = notifications,
                key = {
                    it.notificationId
                }
            ) { notification ->
                NotificationItem(
                    notification = notification,
                    onDeclineClick = onDeclineClick
                )
            }
        }
        CodeTogetherAnimatedVerticalScrollBar(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxHeight(),
            state = state,
            visible = state.canScrollForward
        )
    }
}

@Composable
@Preview
private fun NotificationsPreview() {
    BackgroundPreview {
        Notifications(
            notifications = buildList {
                repeat(10) {
                    val senderName = listOf("Dmitry", "Alex", "sender").random()
                    val notification = Notification(
                        notificationId = it,
                        senderName = senderName,
                        sendTime = "...",
                        projectId = 1,
                        projectName = "$senderName Project"
                    )
                    add(notification)
                }
            },
            state = rememberLazyListState(),
            onDeclineClick = {

            }
        )
    }
}