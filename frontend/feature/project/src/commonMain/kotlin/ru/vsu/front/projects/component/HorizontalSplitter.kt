package ru.vsu.front.projects.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
 * Горизонтальный разделитель, позволяющий пользователю изменять высоту соседних панелей
 *
 * @param modifier Модификатор для настройки.
 * @param cursor Тип указателя мыши (курсора) при наведении на область разделителя.
 * @param onResize Коллбек, вызываемый в процессе перетаскивания.
 * @param color Цвет линии разделителя.
 * @param height Физическая высота области разделителя.
 */
@Composable
fun HorizontalSplitter(
    modifier: Modifier = Modifier,
    cursor: Int = Cursor.S_RESIZE_CURSOR,
    onResize: (deltaDp: Dp) -> Unit,
    color: Color = CodeTogetherTheme.colors.primaryBackground,
    height: Dp = 4.dp,
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .background(color)
            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(cursor)))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onResize(with(density) { dragAmount.y.toDp() })
                }
            }
    )
}