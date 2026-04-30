package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.Project

/**
 * Карточка проекта.
 *
 * @param project Данные проекта.
 * @param modifier Модификатор для настройки.
 * @param shape Форма карточки проекта.
 * @param backgroundColor Цвет фона карточки.
 * @param onProjectClick Коллбек, вызываемый при нажатии на сам проект.
 */
@Composable
internal fun ProjectItem(
    project: Project,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.015f),
    onProjectClick: () -> Unit,
) {
    Surface(
        onClick = onProjectClick,
        modifier = modifier,
        shape = shape,
        contentColor = Color.White,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CodeTogetherText(
                text = project.name,
                color = CodeTogetherTheme.colors.primaryText,
                style = CodeTogetherTheme.typography.style.copy(fontSize = 16.sp)
            )
        }
    }
}