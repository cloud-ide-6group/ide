package ru.vsu.front.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import front.feature.profile.generated.resources.Res
import front.feature.profile.generated.resources.edit_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Аватар пользователя с возможностью его изменения.
 *
 * Загружает изображение через Coil. При наведении курсора поверх картинки
 * появляется иконка редактирования, которая масштабируется ровно на 50%
 * от размера аватара.
 *
 * @param photoPath ?.
 * @param modifier Модификатор для настройки.
 * @param shape Форма изображения.
 * @param onClick Коллбек, вызываемый при клике на аватар.
 */
@Composable
internal fun UserAvatar(
    photoPath: String,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    onClick: () -> Unit = {

    }
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight(),
            model = photoPath,
            contentDescription = "User Avatar",
            contentScale = ContentScale.FillHeight
        )

        if (isHovered) {
            Icon(
                modifier = Modifier
                    .matchParentSize()
                    .scale(0.5f),
                painter = painterResource(Res.drawable.edit_24dp),
                contentDescription = "Change User Avatar",
                tint = CodeTogetherTheme.colors.primary.copy(alpha = 0.75f),
            )
        }
    }
}