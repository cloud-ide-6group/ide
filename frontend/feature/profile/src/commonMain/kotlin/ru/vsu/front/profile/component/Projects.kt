package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.model.entity.Project

/**
 * Список проектов.
 *
 * @param projects Список проектов.
 * @param state Состояние прокрутки списка [LazyListState], используется также для привязки скроллбара.
 * @param modifier Модификатор для настройки.
 * @param isSubscriptionExpired Закончилось ли время платной подписки.
 * @param onProjectClick Коллбек, возвращающий ID проекта при клике на его карточку.
 * @param onProjectInfoClick Коллбек, срабатывающий при клике на иконку информации о проекте.
 */
@Composable
internal fun Projects(
    projects: List<Project>,
    state: LazyListState,
    modifier: Modifier = Modifier,
    isSubscriptionExpired: Boolean = true,
    onProjectClick: (Int) -> Unit,
    onExpiredProjectClick: () -> Unit,
    onProjectInfoClick: (Int) -> Unit,
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        itemsIndexed(items = projects, key = { _, item -> item.id }) { index, item ->
            ProjectItem(
                project = item,
                onIsNotExpiredProjectClick = {
                    onProjectClick(item.id)
                },
                onProjectInfoClick = onProjectInfoClick,
                onExpiredProjectClick = onExpiredProjectClick,
                expired = index > 4 && isSubscriptionExpired
            )
        }
    }
}



@Composable
@Preview
internal fun ProjectsPreview() {
    BackgroundPreview {
        Projects(
            projects = buildList {
                repeat(10) {
                    val project = Project(id = it, name = "Project $it")
                    add(project)
                }
            },
            state = rememberLazyListState(),
            onProjectClick = { _ ->

            },
            onProjectInfoClick = {

            },
            onExpiredProjectClick = {

            }
        )
    }
}