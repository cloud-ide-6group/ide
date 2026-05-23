package ru.vsu.front.projectinfo.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.FileNode

/**
 * Компонент файла.
 *
 * @param file Объект файла.
 * @param modifier Модификатор для настройки.
 * @param offset Отступ слева, вычисляемый на основе уровня элемента в дереве.
 * @param isOpen Открыта ли папка.
 * @param onToggleOpen Коллбек, вызываемый при клике на папку для изменения её состояния.
 * @param onWidthMeasured Коллбек, возвращающий ширину элемента после его отрисовки
 */
@Composable
fun FileItem(
    file: FileNode,
    modifier: Modifier = Modifier,
    offset: Dp,
    isOpen: Boolean,
    onToggleOpen: () -> Unit,
    onWidthMeasured: (Dp) -> Unit
) {
    val mainIcon = if (file.isFolder) AppIcons.Folder else AppIcons.File

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val textColor = if (isHovered) CodeTogetherTheme.colors.primary else CodeTogetherTheme.colors.primaryText

    val density = LocalDensity.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (file.isFolder) onToggleOpen()
                }
            )
            .padding(start = 8.dp + offset)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                val innerWidth = with(density) { coordinates.size.width.toDp() }
                val totalRequiredWidth = 8.dp + offset + innerWidth + 16.dp
                onWidthMeasured(totalRequiredWidth)
            }
        ) {
            if (file.isFolder) {
                val arrowIcon = if (isOpen) AppIcons.ArrowDown else AppIcons.ArrowRight
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(arrowIcon),
                    contentDescription = "Expand/Collapse",
                    tint = Color.White
                )
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(mainIcon),
                contentDescription = if (file.isFolder) "Folder" else "File",
                tint = Color.White
            )

            CodeTogetherText(
                text = file.name,
                color = textColor,
            )
        }
    }
}