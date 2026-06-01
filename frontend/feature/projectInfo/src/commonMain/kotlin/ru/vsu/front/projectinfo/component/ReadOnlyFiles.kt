package ru.vsu.front.projectinfo.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.FileNode

/**
 * Компонент для отображения файлов.
 *
 * @param nodes Список файлов.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun ReadOnlyFiles(
    nodes: List<FileNode>,
    modifier: Modifier = Modifier
) {
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

    BoxWithConstraints(
        modifier = modifier.fillMaxHeight()
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
            renderReadOnlyTreeNodes(
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
                }
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
}

/**
 * Рекурсивно обходит дерево файлов и собирает идентификаторы всех элементов.
 *
 * @param nodes Список файлов.
 * @param expandedIds Идентификаторы раскрытых папок.
 *
 * @return Идентификаторы видимых узлов [Set].
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
 * Функция-расширение, "разворачивающая" дерево.
 *
 * @param nodes Список файлов.
 * @param depth Уровень вложенности файла.
 * @param expandedIds Идентификаторы раскрытых папок.
 * @param onToggleNode Коллбек для сворачивания/разворачивания папки по клику.
 * @param onWidthMeasured Коллбек для получения ширины отрисованного элемента.
 */
fun LazyListScope.renderReadOnlyTreeNodes(
    nodes: List<FileNode>,
    depth: Int,
    expandedIds: Set<Int>,
    onToggleNode: (Int) -> Unit,
    onWidthMeasured: (Int, Dp) -> Unit
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
                onWidthMeasured = { w -> onWidthMeasured(node.id, w) }
            )
        }

        if (node.isFolder && isNodeOpen && node.children.isNotEmpty()) {
            renderReadOnlyTreeNodes(
                nodes = node.children,
                depth = depth + 1,
                expandedIds = expandedIds,
                onToggleNode = onToggleNode,
                onWidthMeasured = onWidthMeasured
            )
        }
    }
}