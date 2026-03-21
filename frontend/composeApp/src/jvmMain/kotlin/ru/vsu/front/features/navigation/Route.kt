package ru.vsu.front.features.navigation

import kotlinx.serialization.Serializable

/**
 * Маршруты для навигации.
 */
@Serializable
sealed interface Route {
    /**
     * Название экрана.
     */
    val name: String

    /**
     * Экран аутентификации.
     */

    @Serializable
    data object Auth : Route {
        override val name: String = "AuthScreen"
    }
}