package ru.vsu.front.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Представляет собой компонент, который позволяет выбрать цвет в колесе.
 *
 * @param modifier Modifier для настройки.
 * @param onClick Коллбек, который вызывается при клике на колесо.
 * @param sizeOfPicker Размер компонента.
 */
@Composable
internal fun RgbColorPicker(
    modifier: Modifier = Modifier,
    sizeOfPicker: Dp = 320.dp,
    onClick: (Color) -> Unit
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(sizeOfPicker)
                .clip(CircleShape)
                .background(Brush.sweepGradient(DEFAULT_COLORS_IN_PICKER_GRADIENT))
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val centerX = size.width / 2f
                        val centerY = size.height / 2f

                        val dx = tapOffset.x - centerX
                        val dy = tapOffset.y - centerY

                        val distance = sqrt(dx * dx + dy * dy)
                        val radius = size.width / 2f

                        if (distance <= radius) {
                            val angleRad = atan2(dy, dx)

                            var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat()
                            if (angleDeg < 0) {
                                angleDeg += 360f
                            }

                            val fraction = angleDeg / 360f

                            onClick(getColorForFraction(fraction))
                        }
                    }
                }
        )
    }
}

/**
 * Стандартные цвета в колесе [RgbColorPicker].
 */
private val DEFAULT_COLORS_IN_PICKER_GRADIENT = listOf(
    Color.Green, Color.Yellow, Color.Red, Color.Magenta,
    Color.Blue, Color.Cyan, Color.Green
)

/**
 * Функция для смешивания цветов.
 * Вычисляет цвет между двумя цветами из списка.
 */
private fun getColorForFraction(fraction: Float): Color {
    if (fraction <= 0f) return DEFAULT_COLORS_IN_PICKER_GRADIENT.first()
    if (fraction >= 1f) return DEFAULT_COLORS_IN_PICKER_GRADIENT.last()

    val exactIndex = fraction * (DEFAULT_COLORS_IN_PICKER_GRADIENT.size - 1)

    val startIndex = exactIndex.toInt()
    val endIndex = minOf(startIndex + 1, DEFAULT_COLORS_IN_PICKER_GRADIENT.size - 1)

    val localFraction = exactIndex - startIndex

    val startColor = DEFAULT_COLORS_IN_PICKER_GRADIENT[startIndex]
    val endColor = DEFAULT_COLORS_IN_PICKER_GRADIENT[endIndex]

    return lerp(startColor, endColor, localFraction)
}