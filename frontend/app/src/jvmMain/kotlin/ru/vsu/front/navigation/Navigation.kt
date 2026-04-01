package ru.vsu.front.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.vsu.front.authorization.AuthScreen
import ru.vsu.front.authorization.AuthViewModel
import ru.vsu.front.profile.ProfileScreen
import ru.vsu.front.profile.ProfileViewModel

/**
 * Главный граф навигации приложения.
 *
 * @param navController Контроллер для управления стеком навигации.
 */
@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Auth,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<Route.Auth> {
            val viewModel = koinViewModel<AuthViewModel>()
            AuthScreen(
                authViewModel = viewModel,
                onSuccessAuth = { userId ->
                    navController.navigate(route = Route.Profile(userId))
                }
            )
        }

        composable<Route.Profile> { backStackEntry ->
            val userId = backStackEntry.toRoute<Route.Profile>().userId

            val viewModel = koinViewModel<ProfileViewModel>(
                parameters = {
                    parametersOf(userId)
                }
            )

            ProfileScreen(
                viewModel = viewModel,
            )
        }
    }
}