package ru.vsu.front.authorization.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.vsu.front.designsystem.component.BackgroundPreview

/**
 * Вспомогательный контейнер столбик для распределения
 * левой и правой половин на экранах авторизации.
 *
 * @param modifier Модификатор для настройки.
 * @param content Слот для внутреннего содержимого столбца.
 */
@Composable
internal fun SideColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
@Preview
private fun SideColumnPreview() {
    BackgroundPreview {
        SideColumn {

        }
    }
}