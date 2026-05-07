package ru.vsu.front.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vsu.front.designsystem.component.CodeTogetherScaffold
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.ErrorScreen
import ru.vsu.front.designsystem.component.LoadingScreen
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.notifications.component.Notifications

/**
 * Экран уведомлений.
 *
 * @param viewModel Вьюмодель для экрана уведомлений.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is NotificationsEffect.ShowMessage -> snackbarHostState.showSnackbar(message = event.message)
            }
        }
    }

    CodeTogetherScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        backgroundColor = CodeTogetherTheme.colors.primaryBackground
    ) {
        when(val currentState = uiState) {
            is UiStateNotifications.Loaded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CodeTogetherText(
                        text = "Notifications",
                        style = TextStyle(fontSize = 16.sp, fontFamily = FontFamily.Monospace),
                        color = CodeTogetherTheme.colors.primary
                    )

                    Notifications(
                        state = rememberLazyListState(),
                        notifications = currentState.notifications,
                        onDeclineClick = { notificationId ->
                            viewModel.processCommand(NotificationsCommand.DeleteNotification(notificationId))
                        }
                    )
                }
            }

            UiStateNotifications.Loading -> {
                LoadingScreen()
            }

            UiStateNotifications.Error -> {
                ErrorScreen(
                    onClick = {
                        viewModel.processCommand(NotificationsCommand.RetryObservingNotifications)
                    }
                )
            }
        }
    }
}