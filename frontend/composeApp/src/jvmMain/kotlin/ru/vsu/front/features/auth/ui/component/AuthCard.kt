@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.ui.component

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Базовая карточка-аутентификация (Логин и регистрация)
 *
 * @param modifier Modifier, который будет применён к карточке
 * @param content Слот под контент
 */
@Composable
internal fun AuthCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .width(400.dp),
        colors = CardDefaults.cardColors(
            containerColor = CodeTogetherTheme.colors.primaryBackground,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        content()
    }
}