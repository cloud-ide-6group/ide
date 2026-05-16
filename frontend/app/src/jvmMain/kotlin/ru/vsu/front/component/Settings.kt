package ru.vsu.front.component

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import front.app.generated.resources.Res
import front.app.generated.resources.close_24dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import ru.vsu.front.ThemeSettings
import ru.vsu.front.designsystem.component.CodeTogetherAnimatedVisibility
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CustomDialog
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.designsystem.theme.DEFAULT_PRIMARY_COLOR

/**
 * Диалог, позволяющий менять настройки приложения (первичный цвет темы).
 *
 * @param visible Видимость настроек.
 * @param modifier Modifier для настройки.
 * @param themeSettings Объект класса настройки темы.
 * @param onDismissRequest Колобэк, вызываемый при клике вне области контента (попытка закрыть диалог).
 * @param onColorClick Коллбэк, вызываемый при клике на колесо по цвету.
 * @param content Слот для контента.
 */
@Composable
internal fun Settings(
    visible: Boolean,
    modifier: Modifier = Modifier,
    themeSettings: ThemeSettings = koinInject(),
    onDismissRequest: () -> Unit,
    onColorClick: (Color) -> Unit,
    content: @Composable () -> Unit
) {
    var isRgbColorPickerVisible by remember { mutableStateOf(false) }
    val primaryColor = themeSettings.loadPrimaryColor()

    Box(modifier = modifier.fillMaxSize()) {
        content()
        CustomDialog(
            show = visible,
            onDismissRequest = onDismissRequest
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CodeTogetherTheme.colors.secondaryBackground,
                tonalElevation = 8.dp,
                modifier = modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CodeTogetherText(text = "Settings")

                    Spacer(Modifier.height(16.dp))

                    Surface(
                        modifier = Modifier
                            .width(480.dp),
                        contentColor = Color.White,
                        color = CodeTogetherTheme.colors.primary.copy(alpha = 0.015f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            isRgbColorPickerVisible = !isRgbColorPickerVisible
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CodeTogetherText(text = "Primary color", color = primaryColor)
                            Spacer(Modifier.weight(1f))
                            CodeTogetherIconButton(
                                onClick = {
                                    onColorClick(DEFAULT_PRIMARY_COLOR)
                                },
                                content = {
                                    Icon(
                                        painter = painterResource(Res.drawable.close_24dp),
                                        contentDescription = "Set default primary color",
                                        tint = CodeTogetherTheme.colors.primary
                                    )
                                },
                            )
                            ColorBox(backgroundColor = primaryColor, size = 24.dp)
                        }
                    }

                    CodeTogetherAnimatedVisibility(
                        visible = isRgbColorPickerVisible,
                        enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                        content = {
                            Spacer(Modifier.height(16.dp))
                            RgbColorPicker(
                                onClick = onColorClick
                            )
                        }
                    )
                }
            }
        }
    }
}