package ru.vsu.front.window

/**
 * Представляет границы и положение окна на экране.
 *
 * @property width Ширина доступной области в пикселях.
 * @property height Высота доступной области в пикселях.
 * @property x Координата X левого верхнего угла рабочей области.
 * @property y Координата Y левого верхнего угла рабочей области.
 */
data class WindowBounds(
    val width: Int,
    val height: Int,
    val x: Int,
    val y: Int
)