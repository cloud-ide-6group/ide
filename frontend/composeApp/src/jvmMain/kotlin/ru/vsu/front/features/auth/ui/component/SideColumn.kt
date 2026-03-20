package ru.vsu.front.features.auth.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Вспомогательный контейнер столбик для распределения
 * левой и правой половин на экранах авторизации.
 *
 * @param modifier Модификатор для настройки.
 * @param content Слот для внутреннего содержимого столбца.
 */
@Composable
internal fun RowScope.SideColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}