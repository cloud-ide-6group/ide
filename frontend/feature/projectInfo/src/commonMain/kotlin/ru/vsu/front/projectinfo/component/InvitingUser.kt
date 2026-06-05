package ru.vsu.front.projectinfo.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.CodeTogetherTextField
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Компонент приглашения пользователя.
 *
 * @param modifier Modifier для настройки.
 * @param userEmail Почта пользователя.
 * @param onValueChanged Коллбек, вызывающийся при изменении введенной почты.
 * @param onClickInviteUser Коллбек, вызывающийся при подтверждении приглашения пользователя.
 */
@Composable
fun InvitingUser(
    modifier: Modifier = Modifier,
    userEmail: String,
    onValueChanged: (String) -> Unit,
    onClickInviteUser: (String) -> Unit,
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
                .width(640.dp)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CodeTogetherText(text = "Inviting User")
            CodeTogetherTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = userEmail,
                hint = "User email",
                onValueChange = onValueChanged
            )
            CodeTogetherTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Invite",
                textColor = CodeTogetherTheme.colors.primary,
                onClick = {
                    onClickInviteUser(userEmail)
                }
            )
        }
    }
}