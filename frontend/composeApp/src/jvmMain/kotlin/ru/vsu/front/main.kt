@file:Suppress("SpellCheckingInspection")

package ru.vsu.front

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.compose.viewmodel.koinViewModel
import ru.vsu.front.designsystem.common.NecessaryAppButtons
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.features.auth.di.initKoin
import ru.vsu.front.features.auth.ui.LoginScreen
import ru.vsu.front.features.auth.ui.LoginViewModel
import java.awt.Dimension

/**
 * Точка входа в программу
 */
fun main() {
    initKoin()
    application {
        val windowState = rememberWindowState()
        Window(
            onCloseRequest = ::exitApplication,
            title = "front",
            state = windowState,
            undecorated = true,
            transparent = true
        ) {
            val density = LocalDensity.current
            setupWindow(density)

            App(
                onMinimizeClick = {

                },
                onMaximizeClick = {

                },
                onCloseClick = ::exitApplication
            ) {
                val loginViewModel: LoginViewModel = koinViewModel<LoginViewModel>()
                LoginScreen(onSignUpClick = {}, viewModel = loginViewModel)
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