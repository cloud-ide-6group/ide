package ru.vsu.front.projects.component

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.FileNode

/**
 * Компонент дерева файлов проекта.
 *
 * @param nodes Список узлов файлов и папок проекта.
 * @param modifier Модификатор для настройки.
 * @param onFileClick Коллбек, вызываемый при клике по файлу для его открытия.
 * @param onDeleteClick Коллбек, вызываемый при выборе удаления элемента.
 * @param onCreateFileClick Коллбек, вызываемый при создании нового элемента в выбранной директории.
 * @param onRenameFileClick Коллбек, вызываемый при переименовании файла или папки.
 */
@Composable
fun Files(
    nodes: List<FileNode>,
    modifier: Modifier = Modifier,
    onFileClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onCreateFileClick: (Int) -> Unit,
    onRenameFileClick: (Int) -> Unit
) {
    var width by remember { mutableStateOf(240.dp) }

    var expandedIds by remember { mutableStateOf(emptySet<Int>()) }
    val itemWidths = remember { mutableStateMapOf<Int, Dp>() }
    val visibleNodeIds = remember(nodes, expandedIds) { getVisibleNodeIds(nodes, expandedIds) }

    val maxContentWidth by remember(visibleNodeIds) {
        derivedStateOf {
            var max = 0.dp
            for (id in visibleNodeIds) {
                val w = itemWidths[id] ?: 0.dp
                if (w > max) max = w
            }
            max
        }
    }

    Row(modifier = modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
        ) {
            val hScrollState = rememberScrollState()
            val vScrollState = rememberLazyListState()
            val minWidth = this.maxWidth

            LazyColumn(
                state = vScrollState,
                modifier = Modifier
                    .fillMaxHeight()
                    .horizontalScroll(hScrollState)
                    .width(maxOf(minWidth, maxContentWidth))
            ) {
                renderTreeNodes(
                    nodes = nodes,
                    depth = 0,
                    expandedIds = expandedIds,
                    onToggleNode = { nodeId ->
                        expandedIds = if (expandedIds.contains(nodeId)) {
                            expandedIds - nodeId
                        } else {
                            expandedIds + nodeId
                        }
                    },
                    onWidthMeasured = { id, measuredWidth ->
                        if (itemWidths[id] != measuredWidth) {
                            itemWidths[id] = measuredWidth
                        }
                    },
                    onFileClick = onFileClick,
                    onDeleteClick = onDeleteClick,
                    onCreateFileClick = onCreateFileClick,
                    onRenameFileClick = onRenameFileClick
                )
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(vScrollState),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                    hoverColor = CodeTogetherTheme.colors.primary
                )
            )

            HorizontalScrollbar(
                adapter = rememberScrollbarAdapter(hScrollState),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(end = 14.dp),
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = CodeTogetherTheme.colors.primary.copy(alpha = 0.1f),
                    hoverColor = CodeTogetherTheme.colors.primary
                )
            )
        }

        VerticalSplitter(
            onResize = {
                width = (width + it).coerceIn(MIN_WIDTH_FILES, MAX_WIDTH_FILES)
            }
        )
    }
}

private val MIN_WIDTH_FILES = 80.dp
private val MAX_WIDTH_FILES = 450.dp

/**
 * Функция для обхода дерева файлов и сбора идентификаторов всех видимых узлов.
 *
 * @param nodes Список корневых узлов файлового дерева.
 * @param expandedIds Множество идентификаторов папок, которые в данный момент раскрыты пользователем.
 *
 * @return Набор идентификаторов всех элементов, которые должны отображаться на экране.
 */
private fun getVisibleNodeIds(nodes: List<FileNode>, expandedIds: Set<Int>): Set<Int> {
    val result = mutableSetOf<Int>()
    fun traverse(list: List<FileNode>) {
        for (node in list) {
            result.add(node.id)
            if (node.isFolder && expandedIds.contains(node.id)) {
                traverse(node.children)
            }
        }
    }
    traverse(nodes)
    return result
}

/**
 * Рекурсивная функция расширения для [LazyListScope], преобразующая дерево файлов в список элементов.
 *
 * @param nodes Список файловых узлов текущего уровня вложенности.
 * @param depth Текущая глубина иерархии.
 * @param expandedIds Множество идентификаторов папок, которые в данный момент раскрыты.
 * @param onToggleNode Коллбек для сворачивания/разворачивания папки по клику.
 * @param onWidthMeasured Коллбек, возвращающий измеренную ширину элемента для расчета горизонтального скролла.
 * @param onFileClick Коллбек открытия файла в редакторе.
 * @param onDeleteClick Коллбек удаления выбранного файла/папки.
 * @param onCreateFileClick Коллбек создания нового элемента внутри выбранной директории.
 * @param onRenameFileClick Коллбек переименования выбранного элемента.
 */
fun LazyListScope.renderTreeNodes(
    nodes: List<FileNode>,
    depth: Int,
    expandedIds: Set<Int>,
    onToggleNode: (Int) -> Unit,
    onWidthMeasured: (Int, Dp) -> Unit,
    onFileClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onCreateFileClick: (Int) -> Unit,
    onRenameFileClick: (Int) -> Unit,
) {
    nodes.forEach { node ->
        val isNodeOpen = expandedIds.contains(node.id)

        item(key = node.id) {
            FileItem(
                modifier = Modifier.animateItem(),
                file = node,
                offset = (depth * 24).dp,
                isOpen = isNodeOpen,
                onToggleOpen = { onToggleNode(node.id) },
                onWidthMeasured = { w -> onWidthMeasured(node.id, w) },
                onLeftClick = onFileClick,
                onDeleteClick = onDeleteClick,
                onCreateFileClick = onCreateFileClick,
                onRenameFileClick = onRenameFileClick
            )
        }

        if (node.isFolder && isNodeOpen && node.children.isNotEmpty()) {
            renderTreeNodes(
                nodes = node.children,
                depth = depth + 1,
                expandedIds = expandedIds,
                onToggleNode = onToggleNode,
                onWidthMeasured = onWidthMeasured,
                onFileClick = onFileClick,
                onDeleteClick = onDeleteClick,
                onCreateFileClick = onCreateFileClick,
                onRenameFileClick = onRenameFileClick
            )
        }
    }
}