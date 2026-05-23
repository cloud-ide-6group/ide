package ru.vsu.front.projects.component

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.FileNode

/**
 * Панель со списком недавно открытых файлов в верхней части редактора.
 *
 * @param files Список файлов.
 * @param modifier Модификатор для.
 * @param selectedFileId Идентификатор файла, открытого в редакторе в данный момент.
 * @param onClick Коллбек, срабатывающий при клике на вкладку для переключения файла.
 * @param onCloseClick Коллбек, вызываемый при нажатии на крестик для закрытия вкладки.
 */
@Composable
fun RecentlyFiles(
    files: List<FileNode>,
    modifier: Modifier = Modifier,
    selectedFileId: Int?,
    onClick: (Int) -> Unit,
    onCloseClick: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()

    HorizontalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState),
        style = LocalScrollbarStyle.current.copy(
            unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
            hoverColor = CodeTogetherTheme.colors.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        files.forEach { fileNode ->
            key(fileNode.id) {
                IdeTabButton(
                    file = fileNode,
                    selected = fileNode.id == selectedFileId,
                    onClick = {
                        onClick(it)
                    },
                    onCloseClick = onCloseClick
                )
            }
        }
    }
}