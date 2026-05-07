package ru.vsu.front.notifications.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
fun Notifications(
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