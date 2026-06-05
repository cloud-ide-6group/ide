package ru.vsu.front.projects.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vsu.front.designsystem.component.CodeTogetherText

/**
 * Компонент для отображения списка строк вывода терминала.
 *
 * @param lines Список текстовых строк вывода терминала.
 * @param modifier Модификатор для.
 * @param state Состояние прокрутки списка.
 */
@Composable
fun TerminalLines(
    lines: List<String>,
    modifier: Modifier = Modifier,
    state: LazyListState,
) {
    LazyColumn(modifier = modifier, state = state) {
        itemsIndexed(items = lines, key = { index, _ -> index }) { _, item ->
            CodeTogetherText(text = item, maxLines = Int.MAX_VALUE)
        }
    }
}