package ru.vsu.front.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.WindowScope
import front.core.designsystem.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.NecessaryAppButtons
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Верхняя панель управления.
 * * Предоставляет область для перемещения окна ([WindowDraggableArea]) и содержит
 * базовые системные кнопки управления ([NecessaryAppButtons]).
 *
 * @param onMinimizeClick Коллбек, вызываемый при клике на кнопку "Свернуть".
 * @param onMaximizeClick Коллбек, вызываемый при клике на кнопку "Свернуть в окно".
 * @param onCloseClick Коллбек, вызываемый при клике на кнопку "Закрыть".
 * @param content Слот для добавления пользовательского контента в левую часть панели.
 */
@Composable
fun WindowScope.WindowTopBar(
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    content: @Composable RowScope.() -> Unit = {}
) {
    WindowDraggableArea(
        modifier = Modifier
            .fillMaxWidth()
            .height(NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP.dp)
            .background(CodeTogetherTheme.colors.primaryBackground)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val appIconRes = Res.drawable.app_icon_without_background
                Icon(
                    modifier = Modifier
                        .size(NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP.dp),
                    painter = painterResource(appIconRes),
                    contentDescription = "Code Together",
                    tint = Color.Unspecified
                )

                Spacer(Modifier.weight(1f))

                NecessaryAppButtons.entries.fastForEach { entry ->
                    val (onClick, iconRes) = when (entry) {
                        NecessaryAppButtons.Minimize -> {
                            onMinimizeClick to Res.drawable.minimize_24dp
                        }

                        NecessaryAppButtons.Maximize -> {
                            onMaximizeClick to Res.drawable.maximize_24dp
                        }

                        NecessaryAppButtons.Close -> {
                            onCloseClick to Res.drawable.close_24dp
                        }
                    }

                    CodeTogetherIconButton(
                        modifier = Modifier
                            .size(NecessaryAppButtons.NECESSARY_BUTTON_SIZE_IN_DP.dp),
                        onClick = onClick
                    ) {
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = entry.name,
                            tint = CodeTogetherTheme.colors.primary,
                        )
                    }
                }
            }
        }
    }
}