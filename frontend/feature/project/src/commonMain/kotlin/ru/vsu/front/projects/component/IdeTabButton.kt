package ru.vsu.front.projects.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.FileNode

/**
 * Компонент отдельной вкладки файла для панели недавних файлов.
 *
 * @param file Модель файла.
 * @param modifier Модификатор для настройки.
 * @param selected Флаг, указывающий, выбран ли данный файл в редакторе прямо сейчас.
 * @param onClick Коллбек, вызываемый при клике по вкладке левой кнопкой мыши.
 * @param onCloseClick Коллбек, вызываемый при нажатии на иконку удаления файла из недавних.
 */
@Composable
fun IdeTabButton(
    file: FileNode,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: (Int) -> Unit,
    onCloseClick: (Int) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        selected -> CodeTogetherTheme.colors.primary.copy(alpha = 0.1f)
        isHovered -> Color.White.copy(alpha = 0.05f)
        else -> Color.Transparent
    }

    val textColor = if (selected || isHovered) {
        CodeTogetherTheme.colors.primaryText
    } else {
        CodeTogetherTheme.colors.secondaryText
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onClick(file.id)
                }
            )
            .padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = file.name,
            color = textColor,
            style = TextStyle(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            )
        )

        val iconSize = 18.dp
        if (isHovered || selected) {
            CodeTogetherIconButton(
                modifier = Modifier.size(iconSize),
                hoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                onClick = {
                    onCloseClick(file.id)
                }
            ) {
                Icon(
                    painter = painterResource(AppIcons.Close),
                    contentDescription = "Delete file from recently files",
                    tint = Color.White,
                )
            }
        } else {
            Spacer(modifier = Modifier.size(iconSize))
        }
    }
}