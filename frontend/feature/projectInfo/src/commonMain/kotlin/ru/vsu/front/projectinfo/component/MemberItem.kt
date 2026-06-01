package ru.vsu.front.projectinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.User

/**
 * Карточка участника.
 *
 * @param member Объект пользователя.
 * @param isOwner Является ли текущий пользователь создателем проекта
 * @param modifier Modifier для настройки.
 * @param onKickClick Коллбек, вызывающийся при клике на кнопку исключения пользователя.
 */
@Composable
fun MemberItem(
    member: User,
    isOwner: Boolean,
    modifier: Modifier = Modifier,
    onKickClick: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CodeTogetherTheme.colors.primary.copy(alpha = 0.025f))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CodeTogetherText(text = member.name)
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.height(48.dp)){
            if (isOwner) {
                CodeTogetherIconButton(
                    onClick = {
                        onKickClick(member.email)
                    }
                ) {
                    Icon(
                        painter = painterResource(AppIcons.Remove),
                        contentDescription = "Kick user",
                        tint = CodeTogetherTheme.colors.primary,
                    )
                }
            }
        }
    }
}