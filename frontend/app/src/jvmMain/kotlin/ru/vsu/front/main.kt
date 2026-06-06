package ru.vsu.front

import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.common.Const
import ru.vsu.front.component.Settings
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.di.initKoin
import ru.vsu.front.navigation.Navigation
import ru.vsu.front.network.MainHttpClientManager
import ru.vsu.front.settings.Settings
import ru.vsu.front.window.DesktopScreenMetricsProvider
import java.awt.Dimension

private const val MIN_WINDOW_WIDTH = 1000
private const val MIN_WINDOW_HEIGHT = 720

/**
 * Точка входа в приложение.
 * * Инициализирует DI.
 * * Управляет глобальным состоянием окна.
 */
fun main() {
    initKoin()
    application {
        val windowState = rememberWindowState(
            width = MIN_WINDOW_WIDTH.dp,
            height = MIN_WINDOW_HEIGHT.dp
        )

        val settings: Settings = koinInject()
        val mainHttpClientManager: MainHttpClientManager = koinInject()
        val authManager: AuthManager = koinInject()

        Window(
            onCloseRequest = {
                exitApplication()
            },
            title = Const.APP_NAME,
            icon = painterResource(AppIcons.AppIcon),
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

            val navController = rememberNavController()

            var isSettingsVisible by remember { mutableStateOf(false) }

            App {
                Settings(
                    visible = isSettingsVisible,
                    onDismissRequest = {
                        isSettingsVisible = false
                    },
                    onColorClick = { color ->
                        settings.savePrimaryColor(color)
                    },
                    content = {
                        Navigation(
                            navController = navController,
                            onMinimizeClick = {
                                windowState.onMinimizeClick()
                            },
                            onMaximizeClick = {
                                windowState.onMaximizeClick(
                                    previousPosition = previousPosition,
                                    previousSize = previousSize,
                                    onPreviousPositionChange = {
                                        previousPosition = it
                                    },
                                    onPreviousSizeChange = {
                                        previousSize = it
                                    }
                                )
                            },
                            onCloseClick = {
                                exitApplication()
                            },
                            onSettingsClick = {
                                isSettingsVisible = true
                            },
                            onLogoutClick = {
                                authManager.logout()
                                mainHttpClientManager.invalidateClient()

                            }
                        )
                    }
                )
            }
        }
    }
}

/**
 * Задает минимально допустимые размеры окна.
 */
@Composable
private fun WindowScope.setupWindow() {
    LaunchedEffect(Unit) {
        window.minimumSize = Dimension(
            MIN_WINDOW_WIDTH,
            MIN_WINDOW_HEIGHT
        )
    }
}

/**
 * Вызывается при клике на кнопку "Свернуть".
 */
private fun WindowState.onMinimizeClick() {
    isMinimized = true
}


/**
 * Вызывается при клике на кнопку "Свернуть в окно".
 *
 * @param previousPosition Предыдущая позиция окна.
 * @param previousSize Предыдущий размер окна.
 * @param onPreviousPositionChange Коллбек, который вызывается когда меняется позиция окна.
 * @param onPreviousSizeChange Коллбек, который вызывается когда меняется размер окна.
 */
private inline fun WindowState.onMaximizeClick(
    previousPosition: WindowPosition,
    previousSize: DpSize,
    onPreviousPositionChange: (WindowPosition) -> Unit,
    onPreviousSizeChange: (DpSize) -> Unit
) {
    val bounds = DesktopScreenMetricsProvider.getMaximumWindowBounds()

    val maxWidth = bounds.width.dp
    val maxHeight = bounds.height.dp
    val maxX = bounds.x.dp
    val maxY = bounds.y.dp

    val isCurrentlyMaximized =
        maxWidth.value - size.width.value < 1.5f &&
                maxHeight.value - size.height.value < 1.5f

    if (isCurrentlyMaximized) {
        position = previousPosition
        size = previousSize
    } else {
        onPreviousSizeChange(size)
        onPreviousPositionChange(position)
        position = WindowPosition(maxX, maxY)
        size = DpSize(maxWidth, maxHeight)
    }
}