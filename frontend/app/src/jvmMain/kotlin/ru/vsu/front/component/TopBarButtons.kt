package ru.vsu.front.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import front.app.generated.resources.Res
import front.app.generated.resources.back_24dp
import front.app.generated.resources.logout_24dp
import front.app.generated.resources.notifications_24dp
import front.app.generated.resources.settings_24dp
import ru.vsu.front.navigation.Route

/**
 * Компонент для отображения дополнительных (не дефолтных) кнопок верхней панели
 * в зависимости от текущего экрана.
 *
 * @param navDestination Текущий экран навигации, на основе которого определяется набор кнопок.
 * @param onLogoutClick Коллбек, вызываемый при нажатии на кнопку выхода из аккаунта.
 * @param onNotificationsClick Коллбек, вызываемый при нажатии на кнопку перехода к уведомлениям.
 * @param onBackClick Коллбек, вызываемый при нажатии на кнопку возврата на предыдущий экран.
 */
@Composable
fun TopBarButtons(
    navDestination: NavDestination?,
    onLogoutClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    when {
        navDestination?.hasRoute<Route.Profile>() == true -> {
            TopBarButton(onClick = {}, icon = Res.drawable.settings_24dp)
            TopBarButton(onClick = onLogoutClick, icon = Res.drawable.logout_24dp)
            TopBarButton(onClick = onNotificationsClick, icon = Res.drawable.notifications_24dp)
        }
        navDestination?.hasRoute<Route.Notifications>() == true -> {
            TopBarButton(onClick = {}, icon = Res.drawable.settings_24dp)
            TopBarButton(onClick = onLogoutClick, icon = Res.drawable.logout_24dp)
            TopBarButton(onClick = onBackClick, icon = Res.drawable.back_24dp)
        }
        navDestination?.hasRoute<Route.ProjectInfo>() == true -> {
            TopBarButton(onClick = {}, icon = Res.drawable.settings_24dp)
            TopBarButton(onClick = onLogoutClick, icon = Res.drawable.logout_24dp)
            TopBarButton(onClick = onBackClick, icon = Res.drawable.back_24dp)
        }
    }
}