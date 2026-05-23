package ru.vsu.front.projects.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skiko.Cursor
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Вертикальный разделитель, позволяющий пользователю изменять ширину.
 *
 * @param modifier Модификатор для настройки макета разделителя.
 * @param cursor Тип указателя мыши при наведении на разделитель.
 * @param onResize Коллбек, вызываемый при перетаскивании разделителя, возвращает дельту изменения ширины.
 * @param color Цвет линии разделителя.
 * @param width Ширина интерактивной области разделителя.
 */
@Composable
fun VerticalSplitter(
    modifier: Modifier = Modifier,
    cursor: Int = Cursor.W_RESIZE_CURSOR,
    onResize: (deltaDp: Dp) -> Unit,
    color: Color = CodeTogetherTheme.colors.primaryBackground,
    width: Dp = 4.dp
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(color)
            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(cursor)))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onResize(with(density) { dragAmount.x.toDp() })
                }
            }
    )
}