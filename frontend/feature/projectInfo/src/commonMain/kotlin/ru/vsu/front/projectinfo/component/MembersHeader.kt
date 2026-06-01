package ru.vsu.front.projectinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Компонент заголовка участников.
 *
 * @param membersAreVisible Видны ли пользователи.
 * @param modifier Modifier для настройки.
 * @param onInviteClick Коллбек, вызывающийся при клике на кнопку пригласить.
 * @param onToggleVisibilityClick Коллбек, вызывающийся при клике на кнопку раскрыть/свернуть.
 */
@Composable
fun MembersHeader(
    membersAreVisible: Boolean,
    isOwner: Boolean,
    modifier: Modifier = Modifier,
    onInviteClick: () -> Unit,
    onToggleVisibilityClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CodeTogetherTheme.colors.primary.copy(alpha = 0.05f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CodeTogetherText(
            text = "Members",
            style = TextStyle(
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isOwner) {
            CodeTogetherIconButton(
                onClick = {
                    onInviteClick()
                },
                hoverColor = Color.White.copy(alpha = 0.1f),
            ) {
                Icon(
                    painter = painterResource(AppIcons.Add2),
                    contentDescription = "Invite user",
                    tint = CodeTogetherTheme.colors.primary
                )
            }
        }
        CodeTogetherIconButton(
            onClick = {
                onToggleVisibilityClick()
            },
            hoverColor = Color.White.copy(alpha = 0.1f),
        ) {
            Icon(
                painter = painterResource(if (membersAreVisible) AppIcons.CodeVertical else AppIcons.CodeHorizontal),
                contentDescription = "Toggle members visibility",
                tint = CodeTogetherTheme.colors.primary
            )
        }
    }
}