package ru.vsu.front.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import front.feature.authorization.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.authorization.component.AuthForm
import ru.vsu.front.authorization.component.LeftSide
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherScaffold
import ru.vsu.front.designsystem.component.TopBarButton

/**
 * Экран авторизации и регистрации.
 *
 * @param authViewModel Вьюмодель для входа и регистрации.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun WindowScope.AuthScreen(
    authViewModel: AuthViewModel,
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    onSettingsClick: () -> Unit,
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

    CodeTogetherScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        onMinimizeClick = onMinimizeClick,
        onMaximizeClick = onMaximizeClick,
        onCloseClick = onCloseClick,
        topBarContent = {
            TopBarButton(
                onClick = onSettingsClick,
                icon = AppIcons.Settings
            )
        },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                alpha = 0.025f,
                painter = painterResource(AppIcons.AppIconWithoutBackground),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
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