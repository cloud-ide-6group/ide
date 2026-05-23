package ru.vsu.front.projects.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.CodeTogetherTextField
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Панель терминала.
 *
 * @param lines Список текстовых строк консоли, получаемых от запущенной программы.
 * @param consoleInput Текущий текст, введенный пользователем в поле ввода терминала.
 * @param modifier Модификатор для настройки.
 * @param onCloseClick Коллбек, срабатывающий при нажатии на кнопку закрытия терминала.
 * @param onClearClick Коллбек для очистки текста в консоли.
 * @param onInputChange Коллбек, вызываемый при изменении текущего ввода.
 * @param onEnterPressed Коллбек, срабатывающий при нажатии клавиши Enter.
 */
@Composable
fun TerminalPanel(
    lines: List<String>,
    consoleInput: String,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onClearClick: () -> Unit,
    onInputChange: (String) -> Unit,
    onEnterPressed: (() -> Unit)? = null,
) {
    var terminalPanelHeight by remember { mutableStateOf(240.dp) }
    val terminalLinesState = rememberLazyListState()
    LaunchedEffect(lines) {
        terminalLinesState.animateScrollToItem(lines.size)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(terminalPanelHeight)
            .background(CodeTogetherTheme.colors.primaryBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            HorizontalSplitter(onResize = { delta ->
                terminalPanelHeight = (terminalPanelHeight - delta).coerceIn(MIN_TERMINAL_HEIGHT, MAX_TERMINAL_HEIGHT)
            })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CodeTogetherText(
                    text = "Terminal",
                    color = CodeTogetherTheme.colors.primary
                )

                Spacer(modifier = Modifier.weight(1f))

                CodeTogetherTextButton(
                    text = "Clear",
                    onClick = onClearClick,
                    textColor = CodeTogetherTheme.colors.primary
                )

                CodeTogetherIconButton(
                    onClick = onCloseClick
                ) {
                    Icon(
                        painter = painterResource(AppIcons.Close),
                        contentDescription = "Close Terminal",
                        tint = CodeTogetherTheme.colors.primary
                    )
                }
            }

            TerminalLines(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(CodeTogetherTheme.colors.secondaryBackground)
                    .padding(8.dp),
                lines = lines,
                state = terminalLinesState
            )

            CodeTogetherTextField(
                modifier = Modifier.fillMaxWidth(),
                value = consoleInput,
                onValueChange = onInputChange,
                onEnterPressed = onEnterPressed
            )
        }

        val unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f)
        val hoverColor = CodeTogetherTheme.colors.primary
        VerticalScrollbar(
            modifier = modifier,
            adapter = rememberScrollbarAdapter(terminalLinesState),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = unhoverColor,
                hoverColor = hoverColor
            )
        )
    }
}

val MIN_TERMINAL_HEIGHT = 120.dp
val MAX_TERMINAL_HEIGHT = 560.dp