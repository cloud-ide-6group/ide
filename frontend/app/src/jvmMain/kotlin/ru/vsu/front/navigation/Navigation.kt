package ru.vsu.front.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.auth.AuthState
import ru.vsu.front.authorization.AuthScreen
import ru.vsu.front.authorization.AuthViewModel
import ru.vsu.front.profile.ProfileScreen
import ru.vsu.front.profile.ProfileViewModel

/**
 * Главный граф навигации приложения.
 *
 * @param navController Контроллер для управления стеком навигации.
 * @param AuthManager Менеджер аутентификации, предоставляющий состояние текущей сессии пользователя.
 */
@Composable
fun Navigation(
    navController: NavHostController,
    authManager: AuthManager = koinInject()
) {
    val session by authManager.isAuthorized.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if(session is AuthState.Authorized) {
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
                authViewModel = viewModel
            )
        }

        composable<Route.Profile> {
            val viewModel = koinViewModel<ProfileViewModel>()
            ProfileScreen(
                viewModel = viewModel
            )
        }
    }
}