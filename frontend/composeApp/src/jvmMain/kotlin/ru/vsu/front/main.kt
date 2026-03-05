package ru.vsu.front

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.vsu.front.features.auth.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "front",
        ) {
            App()
        }
    }
}