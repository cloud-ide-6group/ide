@file:OptIn(ExperimentalFoundationApi::class)

package ru.vsu.front.projects.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
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
 * @param offset Отступ для поддержки создания дерева файлов.
 * @param isOpen Открыт ли (для папки).
 * @param onToggleOpen Коллбек, вызывающийся при нажатии на папку.
 * @param onWidthMeasured Коллбек, когда ширина компонента была измерена.
 * @param onLeftClick Коллбек, срабатывающий при нажатии лкм по файлу.
 * @param onDeleteClick Коллбек, срабатывающий при клике на удаление файла.
 * @param onCreateFileClick Коллбек, срабатывающий при клике на создание файла.
 * @param onRenameFileClick Коллбек, срабатывающий при клике на переименовывание файла.
 */
@Composable
fun FileItem(
    file: FileNode,
    modifier: Modifier = Modifier,
    offset: Dp,
    isOpen: Boolean,
    onToggleOpen: () -> Unit,
    onWidthMeasured: (Dp) -> Unit,
    onLeftClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onCreateFileClick: (Int) -> Unit,
    onRenameFileClick: (Int) -> Unit,
) {
    val mainIcon = when (file.isFolder) {
        true -> AppIcons.Folder
        false -> AppIcons.File
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val textColor = if (isHovered) CodeTogetherTheme.colors.primary else CodeTogetherTheme.colors.primaryText

    var isMenuExpanded by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
    val density = LocalDensity.current

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .hoverable(interactionSource)
                .onClick(
                    matcher = PointerMatcher.Primary,
                    onClick = {
                        if (file.isFolder) onToggleOpen() else onLeftClick(file.id)
                    }
                )
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                                val position = event.changes.first().position
                                menuOffset = with(density) {
                                    DpOffset(position.x.toDp(), 0.dp)
                                }
                                isMenuExpanded = true
                                event.changes.forEach {
                                    it.consume()
                                }
                            }
                        }
                    }
                }
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

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            offset = menuOffset,
            containerColor = CodeTogetherTheme.colors.secondaryBackground
        ) {
            if (file.isFolder) {
                DropdownMenuItem(
                    text = { CodeTogetherText("Create file") },
                    onClick = {
                        isMenuExpanded = false
                        onCreateFileClick(file.id)
                    }
                )
            }

            DropdownMenuItem(
                text = { CodeTogetherText("Delete") },
                onClick = {
                    isMenuExpanded = false
                    onDeleteClick(file.id)
                }
            )

            DropdownMenuItem(
                text = { CodeTogetherText("Rename") },
                onClick = {
                    isMenuExpanded = false
                    onRenameFileClick(file.id)
                }
            )
        }
    }
}