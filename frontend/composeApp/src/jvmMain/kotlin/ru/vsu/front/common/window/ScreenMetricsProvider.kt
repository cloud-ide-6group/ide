@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.common.window

/**
 * Интерфейс, единственный метод которого возвращает максимальный размер окна
 */
interface ScreenMetricsProvider {
    fun getMaximumWindowBounds(): WindowBounds
}