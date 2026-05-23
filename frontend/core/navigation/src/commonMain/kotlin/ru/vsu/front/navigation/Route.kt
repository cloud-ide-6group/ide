package ru.vsu.front.navigation

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

    /**
     * Экран профиля.
     */
    @Serializable
    data class Profile(val userId: Int) : Route {
        override val name: String = "ProfileScreen"
    }

    /**
     * Экран уведомлений.
     */
    @Serializable
    data object Notifications : Route {
        override val name: String = "NotificationsScreen"
    }

    /**
     * Экран информации о проекте.
     */
    @Serializable
    data class ProjectInfo(val projectId: Int) : Route {
        override val name: String = "ProjectInfoScreen"
    }

    /**
     * Экран проекта.
     */
    @Serializable
    data class Project(val projectId: Int) : Route {
        override val name: String = "ProjectScreen"
    }
}