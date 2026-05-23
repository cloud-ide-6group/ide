package ru.vsu.front.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.auth.AuthState
import ru.vsu.front.authorization.AuthScreen
import ru.vsu.front.authorization.AuthViewModel
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.notifications.NotificationsScreen
import ru.vsu.front.notifications.NotificationsViewModel
import ru.vsu.front.profile.ProfileScreen
import ru.vsu.front.profile.ProfileViewModel
import ru.vsu.front.projectinfo.ProjectInfoScreen
import ru.vsu.front.projectinfo.ProjectInfoViewModel
import ru.vsu.front.projects.ProjectScreen
import ru.vsu.front.projects.ProjectViewModel

/**
 * Главный граф навигации приложения.
 *
 * @param navController Контроллер для управления стеком навигации.
 * @param AuthManager Менеджер аутентификации, предоставляющий состояние текущей сессии пользователя.
 */
@Composable
fun WindowScope.Navigation(
    navController: NavHostController,
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    authManager: AuthManager = koinInject(),
) {
    val session by authManager.isAuthorized.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (session is AuthState.Authorized) {
            Route.Profile((session as AuthState.Authorized).userId)
        } else Route.Auth,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<Route.Auth> {
            val viewModel = koinViewModel<AuthViewModel>()
            AuthScreen(
                authViewModel = viewModel,
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
                onSettingsClick = onSettingsClick
            )
        }

        composable<Route.Profile> {
            val viewModel = koinViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel,
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
                onSettingsClick = onSettingsClick,
                onNotificationsClick = {
                    navController.navigate(Route.Notifications)
                },
                onLogoutClick = onLogoutClick,
                onProjectClick = { projectId ->
                    navController.navigate(Route.Project(projectId))
                },
                onProjectInfoClick = { projectId ->
                    navController.navigate(Route.ProjectInfo(projectId))
                }
            )
        }

        composable<Route.Notifications> {
            val viewModel = koinViewModel<NotificationsViewModel>()
            NotificationsScreen(
                viewModel = viewModel,
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
                onSettingsClick = onSettingsClick,
                onLogoutClick = onLogoutClick,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.ProjectInfo> { navBackStackEntry ->
            val projectId = navBackStackEntry.toRoute<Route.ProjectInfo>().projectId

            val viewModel = koinViewModel<ProjectInfoViewModel>(
                parameters = {
                    parametersOf(projectId)
                }
            )

            ProjectInfoScreen(
                viewModel = viewModel,
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
                onSettingsClick = onSettingsClick,
                onLogoutClick = onLogoutClick,
                onBackClick = {
                    navController.popBackStack()
                },
                onRemovedFromProject = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.Project> { navBackStackEntry ->
            val projectId = navBackStackEntry.toRoute<Route.Project>().projectId

            val viewModel = koinViewModel<ProjectViewModel>(
                parameters = {
                    parametersOf(projectId)
                }
            )

            ProjectScreen(
                viewModel = viewModel,
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
                onSettingsClick = onSettingsClick,
                onLogoutClick = onLogoutClick,
                onBackClick = {
                    navController.popBackStack()
                },
                onRemovedFromProject = {
                    navController.popBackStack()
                }
            )
        }
    }
}