package ru.vsu.front.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Экран ошибки.
 *
 * @param modifier Модификатор для настройки.
 * @param errorText Сообщение об ошибке.
 * @param buttonText Текст, который будет использован в [CodeTogetherTextButton].
 * @param onClick Коллбек, срабатывающий при клике на кнопку.
 */
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorText: String = "Произошла ошибка во время загрузки! :(",
    buttonText: String = "Повторить",
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CodeTogetherText(text = errorText)

        Spacer(modifier = Modifier.height(16.dp))

        CodeTogetherTextButton(text = buttonText, onClick = onClick)
    }
}