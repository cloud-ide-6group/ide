package ru.vsu.front.notifications.component

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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

@Composable
fun Notifications(
    notifications: List<Notification>,
    state: LazyListState,
    modifier: Modifier = Modifier,
    onAcceptClick: (Int) -> Unit,
    onDeclineClick: (Int) -> Unit,
) {
    Row {
        LazyColumn(
            modifier = modifier.weight(1f),
            state = state,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = notifications,
                key = {
                    it.projectId
                }
            ) { notification ->
                NotificationItem(
                    notification = notification,
                    onAcceptClick = onAcceptClick,
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