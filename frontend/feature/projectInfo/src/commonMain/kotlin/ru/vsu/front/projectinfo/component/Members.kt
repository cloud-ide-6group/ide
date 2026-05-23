package ru.vsu.front.projectinfo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.model.entity.User

/**
 * Компонент участников проекта.
 *
 * @param members Участники проекта.
 * @param isOwner Является ли текущий пользователь создателем проекта
 * @param modifier Modifier для настройки.
 * @param onKickClick Коллбек, вызывающийся при клике на кнопку исключения пользователя.
 */
@Composable
fun Members(
    members: List<User>,
    isOwner: Boolean,
    modifier: Modifier = Modifier,
    onKickClick: (String) -> Unit
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = members, key = { it.userId }) { member ->
            MemberItem(
                modifier = Modifier
                    .animateItem(),
                member = member,
                isOwner = isOwner,
                onKickClick = onKickClick,
            )
        }
    }
}