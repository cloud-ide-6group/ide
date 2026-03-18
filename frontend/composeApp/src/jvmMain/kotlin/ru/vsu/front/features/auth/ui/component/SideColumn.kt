package ru.vsu.front.features.auth.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Устранение дублирования, функция для использования корневого столбца в левой и правой части экранов
 * авторизации и регистрации
 *
 * @param modifier Modifier, который будет применён к данной части
 * @param content Слот под контент
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