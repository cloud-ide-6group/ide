package ru.vsu.front

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.navigation.compose.rememberNavController
import front.app.generated.resources.Res
import front.app.generated.resources.app_icon
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.window.DesktopScreenMetricsProvider
import ru.vsu.front.window.WindowBounds
import ru.vsu.front.designsystem.common.NecessaryAppButtons
import ru.vsu.front.di.initKoin
import ru.vsu.front.navigation.Navigation
import java.awt.Dimension
import kotlin.math.abs

/**
 * Точка входа в приложение.
 * * Инициализирует DI.
 * * Управляет глобальным состоянием окна.
 */
fun main() {
    initKoin()
    application {
        val windowState = rememberWindowState().apply {
            size = DpSize(1280.dp, 800.dp)
        }
        Window(
            onCloseRequest = ::exitApplication,
            title = "Code Together",
            icon = painterResource(Res.drawable.app_icon),
            state = windowState,
            undecorated = true,
            transparent = true
        ) {
            var previousSize by remember {
                mutableStateOf(DpSize.Unspecified)
            }
            var previousPosition by remember {
                mutableStateOf<WindowPosition>(WindowPosition.PlatformDefault)
            }

            setupWindow()
            App(
                onMinimizeClick = {
                    windowState.isMinimized = true
                },
                onMaximizeClick = {
                    val bounds = DesktopScreenMetricsProvider.getMaximumWindowBounds()

                    val maxWidth = bounds.width.dp
                    val maxHeight = bounds.height.dp
                    val maxX = bounds.x.dp
                    val maxY = bounds.y.dp

                    val isCurrentlyMaximized =
                        maxWidth.value - windowState.size.width.value < 1.5f &&
                                maxHeight.value - windowState.size.height.value < 1.5f

                    if (isCurrentlyMaximized) {
                        windowState.position = previousPosition
                        windowState.size = previousSize
                    } else {
                        previousSize = windowState.size
                        previousPosition = windowState.position

                        windowState.position = WindowPosition(maxX, maxY)
                        windowState.size = DpSize(maxWidth, maxHeight)
                    }
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
 * Задает минимально допустимые размеры окна.
 * Рассчитывается исходя из размера базовых кастомных кнопок управления окном и иконки приложения.
 */
@Composable
private fun WindowScope.setupWindow() {
    LaunchedEffect(Unit) {
        /**
         * Иконка приложения так же находится в верхней панели, но это не кнопка, так что + 1
         */
        window.minimumSize = Dimension(
            ((NecessaryAppButtons.entries.size + 1) * NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP),
            NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP
        )
    }
}