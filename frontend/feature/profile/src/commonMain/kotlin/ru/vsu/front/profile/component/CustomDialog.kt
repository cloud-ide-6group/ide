package ru.vsu.front.profile.component

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.vsu.front.designsystem.component.CodeTogetherAnimatedVisibility

/**
 * Кастомный диалог.
 *
 *
 * @param show Флаг видимости диалога. `true` - виден, `false` - не виден.
 * @param onDismissRequest Колбэк, вызываемый при клике вне области контента (попытка закрыть диалог).
 * @param content Внутреннее содержимое диалога (например, карточка с формой).
 */
@Composable
internal fun CustomDialog(
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