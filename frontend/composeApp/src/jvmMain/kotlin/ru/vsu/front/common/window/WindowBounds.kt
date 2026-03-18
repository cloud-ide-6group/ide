package ru.vsu.front.common.window

/**
 * @param width Ширина окна
 * @param height Высота окна
 * @param x Положение x окна
 * @param y Положение y окна
 */
data class WindowBounds(
    val width: Int,
    val height: Int,
    val x: Int,
    val y: Int
)