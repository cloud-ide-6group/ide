package ru.vsu.front.projects.component

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextField
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.Message

/**
 * Компонент комментариев (чата).
 *
 * @param messages Список отображаемых сообщений.
 * @param link Строковый идентификатор (ссылка) строки кода, к которой привязан чат.
 * @param inputValue Текущее текстовое значение в поле ввода сообщения.
 * @param onInputChange Коллбек, вызываемый при изменении текста в поле ввода.
 * @param onSendClick Коллбек, срабатывающий при отправке сообщения (нажатии Enter).
 * @param modifier Модификатор для настройки.
 * @param state Состояние прокрутки списка сообщений.
 * @param onCloseClick Коллбек, вызываемый при нажатии на кнопку закрытия панели чата.
 */
@Composable
fun Messages(
    messages: List<Message>,
    link: String,
    inputValue: String,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: ScrollState = rememberScrollState(),
    onCloseClick: () -> Unit
) {
    LaunchedEffect(messages) {
        state.animateScrollTo(100000)
    }

    var width by remember { mutableStateOf(240.dp) }
    Row(
        modifier = modifier
            .fillMaxHeight()
            .width(width),
    ) {
        VerticalSplitter(
            width = 8.dp,
            onResize = {
                width = (width - it).coerceIn(MIN_WIDTH_MESSAGES, MAX_WIDTH_MESSAGES)
            },
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CodeTogetherText(
                    modifier = Modifier.weight(1f),
                    text = link,
                    color = CodeTogetherTheme.colors.primary,
                    style = CodeTogetherTheme.typography.style
                )
                CodeTogetherIconButton(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = onCloseClick
                ) {
                    Icon(
                        painter = painterResource(AppIcons.ArrowRightDown),
                        contentDescription = "Close chat button",
                        tint = CodeTogetherTheme.colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(state),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                messages.forEach { message ->
                    key(message.id) {
                        Message(message = message)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CodeTogetherTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputValue,
                hint = "Напечатать...",
                onValueChange = onInputChange,
                onEnterPressed = onSendClick
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        val unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f)
        val hoverColor = CodeTogetherTheme.colors.primary
        VerticalScrollbar(
            modifier = Modifier,
            adapter = rememberScrollbarAdapter(scrollState = state),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = unhoverColor,
                hoverColor = hoverColor
            )
        )
    }
}

private val MIN_WIDTH_MESSAGES = 80.dp
private val MAX_WIDTH_MESSAGES = 450.dp