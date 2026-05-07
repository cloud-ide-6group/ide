package ru.vsu.front.authorization.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Базовая карточка для форм авторизации и регистрации.
 *
 * @param modifier Модификатор для настройки.
 * @param content Слот для вложенного контента.
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

@Composable
@Preview
private fun AuthCardPreview() {
    BackgroundPreview {
        AuthCard {
            Column(modifier = Modifier.padding(16.dp)) {
                CodeTogetherText("Name")

                Spacer(modifier = Modifier.height(16.dp))

                CodeTogetherText("Email")
            }
        }
    }
}