package ru.vsu.front.window

/**
 * Интерфейс для получения метрик экрана устройства.
 * Позволяет вычислять доступные размеры на разных платформах.
 */
interface ScreenMetricsProvider {
    /**
     * Вычисляет максимальные доступные границы окна для текущего экрана.
     * * @return Объект [WindowBounds] с координатами и размерами доступной области.
     */
    fun getMaximumWindowBounds(): WindowBounds
}