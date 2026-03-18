package ru.vsu.front.features.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import ru.vsu.front.features.auth.ui.LoginScreen
import ru.vsu.front.features.auth.ui.LoginViewModel
import ru.vsu.front.features.auth.ui.SignScreen
import ru.vsu.front.features.auth.ui.SignViewModel
import ru.vsu.front.features.navigation.Route.Login
import ru.vsu.front.features.navigation.Route.Sign

/**
 * Общий интерфейс для экранов
 *
 * @see name Название экрана
 * @see Login Экран логина
 * @see Sign Экран регистрации
 */
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

/**
 * Функция для навигации
 *
 * @see navController Компонент, отвечающий за навигацию
 */
@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Login,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }

    ) {
        composable<Login> {
            val loginViewModel = koinViewModel<LoginViewModel>()
            LoginScreen(
                onSignUpClick = {
                    navController.navigate(Sign)
                },
                viewModel = loginViewModel
            )
        }

        composable<Sign> {
            val signViewModel = koinViewModel<SignViewModel>()
            SignScreen(
                onLoginClick = {
                    navController.navigate(Login)
                },
                viewModel = signViewModel
            )
        }
    }
}