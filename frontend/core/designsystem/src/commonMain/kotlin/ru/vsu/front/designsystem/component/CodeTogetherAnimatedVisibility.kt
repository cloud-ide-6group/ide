package ru.vsu.front.designsystem.component

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Базовая анимация.
 *
 * @param visible Видимость.
 * @param modifier Модификатор корневого контейнера.
 * @param enter Анимация появления.
 * @param exit Анимация исчезания.
 * @param content Слот для контента.
 */
@Composable
fun CodeTogetherAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
    exit: ExitTransition = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut(),
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}