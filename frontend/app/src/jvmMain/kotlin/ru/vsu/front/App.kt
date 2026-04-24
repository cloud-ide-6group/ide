package ru.vsu.front

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowScope
import ru.vsu.front.designsystem.component.WindowTopBar
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.designsystem.theme.CodeTogetherThemeVariant

/**
 * Корневой Composable-компонент, задающий общую структуру окна приложения.
 * Оборачивает контент в глобальную тему [CodeTogetherTheme] и добавляет
 * верхнюю панель управления окном ([WindowTopBar]).
 *
 * @param onMinimizeClick Коллбек, вызываемый при клике на кнопку "Свернуть".
 * @param onMaximizeClick Коллбек, вызываемый при клике на кнопку "Свернуть в окно".
 * @param onCloseClick Коллбек, вызываемый при клике на кнопку "Закрыть".
 * @param topBarContent Слот для содержимого верхней панели.
 * @param content Слот для основного содержимого.
 */
@Composable
fun WindowScope.App(
    onMinimizeClick: () -> Unit,
    onMaximizeClick: () -> Unit,
    onCloseClick: () -> Unit,
    topBarContent: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    CodeTogetherTheme(
        themeVariant = CodeTogetherThemeVariant.Omni
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            WindowTopBar(
                onMinimizeClick = onMinimizeClick,
                onMaximizeClick = onMaximizeClick,
                onCloseClick = onCloseClick,
                content = topBarContent
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(CodeTogetherTheme.colors.primaryBackground)
            ) {
                content()
            }
        }
    }
}