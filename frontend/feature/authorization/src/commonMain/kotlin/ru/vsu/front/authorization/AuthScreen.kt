package ru.vsu.front.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import front.feature.authorization.generated.resources.Res
import front.feature.authorization.generated.resources.app_icon_without_background
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.authorization.component.AuthForm
import ru.vsu.front.designsystem.component.CodeTogetherScaffold
import ru.vsu.front.authorization.component.LeftSide

/**
 * Экран авторизации и регистрации.
 *
 * @param authViewModel Вьюмодель для входа и регистрации.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun AuthScreen(
    onSuccessAuth: (Int) -> Unit,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authViewModel) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(event.message)
                is AuthEffect.SuccessAuth -> onSuccessAuth(event.userId)
            }
        }
    }

    CodeTogetherScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                alpha = 0.025f,
                painter = painterResource(Res.drawable.app_icon_without_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
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
}