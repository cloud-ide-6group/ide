package ru.vsu.front.designsystem.component

import androidx.compose.animation.*
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Базовый вертикальный скролл бар приложения.
 *
 * @param visible Видимость.
 * @param state Состояние LazyColumn для [rememberScrollbarAdapter].
 * @param modifier Модификатор корневого контейнера.
 * @param unhoverColor Цвет когда курсор не наведен.
 * @param hoverColor Цвет когда курсор наведен.
 * @param enter Анимация появления.
 * @param exit Анимация исчезания.
 */
@Composable
fun CodeTogetherAnimatedVerticalScrollBar(
    visible: Boolean,
    state: LazyListState,
    modifier: Modifier = Modifier,
    unhoverColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
    hoverColor: Color = CodeTogetherTheme.colors.primary,
    enter: EnterTransition = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
    exit: ExitTransition = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut()
) {
    CodeTogetherAnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        VerticalScrollbar(
            modifier = modifier,
            adapter = rememberScrollbarAdapter(state),
            style = LocalScrollbarStyle.current.copy(
                unhoverColor = unhoverColor,
                hoverColor = hoverColor
            )
        )
    }
}