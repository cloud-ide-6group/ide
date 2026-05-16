package ru.vsu.front.designsystem.component

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Кастомный диалог.
 *
 *
 * @param show Флаг видимости диалога. `true` - виден, `false` - не виден.
 * @param onDismissRequest Колбэк, вызываемый при клике вне области контента (попытка закрыть диалог).
 * @param content Внутреннее содержимое диалога (например, карточка с формой).
 */
@Composable
fun CustomDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    CodeTogetherAnimatedVisibility(
        visible = show,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismissRequest() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier.animateEnterExit(
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                )
            ) {
                content()
            }
        }
    }
}

@Composable
@Preview
private fun CustomDialogPreview() {
    BackgroundPreview(withPadding = false) {
        CustomDialog(
            show = true,
            onDismissRequest = {

            },
            content = {
                ErrorScreen {

                }
            }
        )
    }
}