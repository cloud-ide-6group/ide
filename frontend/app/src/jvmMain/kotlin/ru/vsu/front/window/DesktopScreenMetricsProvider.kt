package ru.vsu.front.window

import java.awt.GraphicsEnvironment

/**
 * Desktop-реализация провайдера метрик экрана [ScreenMetricsProvider].
 * * Использует Java AWT [GraphicsEnvironment] для вычисления доступной области
 * экрана (без учета панели задач и системных меню).
 */
object DesktopScreenMetricsProvider : ScreenMetricsProvider {
    /**
     * Вычисляет максимальные доступные границы окна для текущего экрана.
     * * @return Объект [WindowBounds] с координатами и размерами доступной области.
     */
    override fun getMaximumWindowBounds(): WindowBounds {
        val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val maximumWindowBounds = env.maximumWindowBounds

        return WindowBounds(
            width = maximumWindowBounds.width,
            height = maximumWindowBounds.height,
            x = maximumWindowBounds.x,
            y = maximumWindowBounds.y
        )
    }
}