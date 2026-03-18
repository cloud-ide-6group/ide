package ru.vsu.front.features.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.vsu.front.features.auth.ui.LoginScreen
import ru.vsu.front.features.auth.ui.LoginViewModel
import ru.vsu.front.features.auth.ui.SignScreen
import ru.vsu.front.features.auth.ui.SignViewModel


@Serializable
sealed interface Route {
    val name: String

    @Serializable
    data object Login : Route {
        override val name: String = "LoginScreen"
    }

    @Serializable
    data object Sign : Route {
        override val name: String = "SignScreen"
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }

    ) {
        composable<Route.Login> {
            val loginViewModel = koinViewModel<LoginViewModel>()
            LoginScreen(
                onSignUpClick = {
                    navController.navigate(Route.Sign)
                },
                viewModel = loginViewModel
            )
        }

        composable<Route.Sign> {
            val signViewModel = koinViewModel<SignViewModel>()
            SignScreen(
                onLoginClick = {
                    navController.navigate(Route.Login)
                },
                viewModel = signViewModel
            )
        }
    }
}