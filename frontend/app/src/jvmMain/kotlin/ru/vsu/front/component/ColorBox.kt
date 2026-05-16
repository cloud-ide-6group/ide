package ru.vsu.front.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Представляет собой компонент, который лишь отображает переданный цвет по в заданной форме.
 *
 * @param backgroundColor Цвет.
 * @param modifier Modifier для настройки.
 * @param size Размер компонента.
 * @param shape Фигура компонента.
 */
@Composable
internal fun ColorBox(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    shape: Shape = CircleShape
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor)
    )
}