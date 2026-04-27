package ru.vsu.front.profile.component

import androidx.compose.animation.*
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.Project

/**
 * Объединение [ProjectsButton] и [Projects].
 *
 * @param projects Список проектов.
 * @param projectsAreVisible Флаг, определяющий, видны ли проекты.
 * @param modifier Модификатор для настройки.
 * @param onChangeVisibleClick Коллбек для переключения видимости списка.
 * @param onCreateProjectClick Коллбек для создания нового проекта.
 * @param onProjectClick Коллбек выбора конкретного проекта.
 */
@Composable
internal fun ProjectsSection(
    projects: List<Project>,
    projectsAreVisible: Boolean,
    modifier: Modifier = Modifier,
    onChangeVisibleClick: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onProjectClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 96.dp)
    ) {
        val projectsState = rememberLazyListState()
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            ProjectsButton(
                projectsAreVisible = projectsAreVisible,
                onChangeVisibleClick = onChangeVisibleClick,
                onCreateProjectClick = onCreateProjectClick
            )

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = projectsAreVisible,
                enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
            ) {
                Projects(
                    modifier = Modifier.weight(1f),
                    projects = projects,
                    state = projectsState,
                    onProjectClick = onProjectClick
                )
            }
        }

        AnimatedVisibility(
            visible = projectsAreVisible,
            enter = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut()
        ) {
            VerticalScrollbar(
                modifier = Modifier.padding(horizontal = 8.dp).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(projectsState),
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                    hoverColor = CodeTogetherTheme.colors.primary
                )
            )
        }
    }
}