package ru.vsu.front

import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import front.app.generated.resources.Res
import front.app.generated.resources.app_icon
import front.app.generated.resources.logout_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.common.Const
import ru.vsu.front.designsystem.common.NecessaryAppButtons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.di.initKoin
import ru.vsu.front.navigation.Navigation
import ru.vsu.front.navigation.Route
import ru.vsu.front.network.MainHttpClientManager
import ru.vsu.front.window.DesktopScreenMetricsProvider
import java.awt.Dimension

/**
 * Точка входа в приложение.
 * * Инициализирует DI.
 * * Управляет глобальным состоянием окна.
 */
fun main() {
    initKoin()
    application {
        val windowState = rememberWindowState(
            width = 1280.dp,
            height = 800.dp
        )

        val authManager: AuthManager = koinInject()
        val mainHttpClientManager: MainHttpClientManager = koinInject()

        Window(
            onCloseRequest = {
                exitApplication()
            },
            title = Const.APP_NAME,
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

            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            App(
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
                topBarContent = {
                    if (currentDestination?.hasRoute<Route.Profile>() == true) {
                        CodeTogetherIconButton(onClick = {
                            authManager.logout()
                            mainHttpClientManager.invalidateClient()
                        }) {
                            Icon(
                                painter = painterResource(Res.drawable.logout_24dp),
                                contentDescription = "Logout",
                                tint = CodeTogetherTheme.colors.primary,
                            )
                        }
                    }
                }
            ) {
                Navigation(
                    navController = navController
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