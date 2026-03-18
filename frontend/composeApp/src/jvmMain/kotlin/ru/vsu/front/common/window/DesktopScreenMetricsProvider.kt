package ru.vsu.front.common.window

import java.awt.GraphicsEnvironment

/**
 * Провайдер для получения максимального размера окна на Desktop
 */
object DesktopScreenMetricsProvider : ScreenMetricsProvider {
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