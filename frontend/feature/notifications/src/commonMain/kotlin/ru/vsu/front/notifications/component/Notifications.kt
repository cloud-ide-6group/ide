package ru.vsu.front.notifications.component

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
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
        VerticalScrollbar(
            modifier = Modifier.padding(horizontal = 8.dp).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(state),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                hoverColor = CodeTogetherTheme.colors.primary
            )
        )
    }
}