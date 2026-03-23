package ru.vsu.front.authorization

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.vsu.front.authorization.component.AuthScaffoldWrapper
import ru.vsu.front.authorization.component.LeftSide

/**
 * Экран авторизации и регистрации.
 *
 * @param authViewModel Вьюмодель для входа и регистрации.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authViewModel) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    AuthScaffoldWrapper(
        modifier = modifier,
        snackbarHostState = snackbarHostState
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            LeftSide(
                modifier = Modifier.weight(1f)
            )
            AuthForm(
                authViewModel = authViewModel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}