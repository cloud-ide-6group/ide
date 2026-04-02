package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.model.entity.Project

/**
 * Список проектов.
 *
 * @param projects Список проектов.
 * @param state Состояние прокрутки списка [LazyListState], используется также для привязки скроллбара.
 * @param modifier Модификатор для настройки.
 * @param onProjectClick Коллбек, возвращающий ID проекта при клике на его карточку.
 */
@Composable
internal fun Projects(
    projects: List<Project>,
    state: LazyListState,
    modifier: Modifier = Modifier,
    onProjectClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = state,
            modifier = modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items = projects, key = { it.id }) {
                ProjectItem(
                    project = it,
                    onProjectClick = {
                        onProjectClick(it.id)
                    }
                )
            }
        }
    }
}