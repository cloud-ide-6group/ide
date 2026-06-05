package ru.vsu.front

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import ru.vsu.front.designsystem.component.WindowTopBar
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.designsystem.theme.CodeTogetherThemeVariant
import ru.vsu.front.settings.Settings

/**
 * Корневой Composable-компонент, задающий общую структуру окна приложения.
 * Оборачивает контент в глобальную тему [CodeTogetherTheme] и добавляет
 * верхнюю панель управления окном ([WindowTopBar]).
 *
 * @param settings Объект настроек приложения, предоставляющий доступ к текущей цветовой теме.
 * @param content Слот для основного содержимого.
 */
@Composable
fun App(
    settings: Settings = koinInject(),
    content: @Composable () -> Unit = {}
) {
    val primaryColor by settings.primaryColor.collectAsStateWithLifecycle()

    CodeTogetherTheme(
        themeVariant = CodeTogetherThemeVariant.Omni,
        primaryColor = primaryColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CodeTogetherTheme.colors.primaryBackground)
        ) {
            content()
        }
    }
}