package ru.vsu.front

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.navigation.compose.rememberNavController
import ru.vsu.front.common.initKoin
import ru.vsu.front.common.window.DesktopScreenMetricsProvider
import ru.vsu.front.common.window.WindowBounds
import ru.vsu.front.designsystem.common.NecessaryAppButtons
import ru.vsu.front.features.navigation.Navigation
import java.awt.Dimension

/**
 * Точка входа в программу
 */
fun main() {
    initKoin()
    application {
        val windowState = rememberWindowState().apply {
            size = DpSize(1280.dp, 800.dp)
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "front",
            state = windowState,
            undecorated = true,
            transparent = true
        ) {
            var isMaximized by remember {
                mutableStateOf(false)
            }
            var previousSize by remember {
                mutableStateOf(DpSize.Unspecified)
            }
            var previousPosition by remember {
                mutableStateOf<WindowPosition>(WindowPosition.PlatformDefault)
            }

            val density = LocalDensity.current
            setupWindow(density)

            App(
                onMinimizeClick = {
                    windowState.isMinimized = true
                },
                onMaximizeClick = {
                    when (isMaximized) {
                        true -> {
                            windowState.position = previousPosition
                            windowState.size = previousSize
                        }

                        false -> {
                            previousSize = windowState.size
                            previousPosition = windowState.position
                            val bounds = DesktopScreenMetricsProvider.getMaximumWindowBounds()
                            changeWindowSizeAndPosition(density, bounds, windowState)
                        }
                    }

                    isMaximized = !isMaximized
                },
                onCloseClick = ::exitApplication
            ) {
                Navigation(
                    navController = rememberNavController()
                )
            }
        }
    }
}

/**
 * Первоначальная настройка окна
 *
 * @param density Коэффициент плотности пикселей
 */
@Composable
private fun WindowScope.setupWindow(density: Density) {
    LaunchedEffect(density) {
        window.minimumSize = with(density) {
            Dimension(
                (NecessaryAppButtons.entries.size * NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP).dp.toPx()
                    .toInt(),
                NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP.dp.toPx().toInt(),
            )
        }
    }
}

/**
 * Изменение размера и положения окна
 *
 * @param density Коэффициент плотности пикселей
 * @param maximumWindowBounds Максимальный размер окна без учета любых панелей
 * @param window Состояник окна
 */
private fun changeWindowSizeAndPosition(
    density: Density,
    maximumWindowBounds: WindowBounds,
    window: WindowState
) {
    val width = with(density) { maximumWindowBounds.width.toDp() }
    val height = with(density) { maximumWindowBounds.height.toDp() }

    val x = with(density) { maximumWindowBounds.x.toDp() }
    val y = with(density) { maximumWindowBounds.y.toDp() }

    window.size = DpSize(width, height)
    window.position = WindowPosition(x, y)
}