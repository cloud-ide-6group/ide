package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.BackgroundPreview
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.Project

/**
 * Карточка проекта.
 *
 * @param project Данные проекта.
 * @param modifier Модификатор для настройки.
 * @param expired Закончилось ли время платной подписки.
 * @param shape Форма карточки проекта.
 * @param backgroundColor Цвет фона карточки.
 * @param onIsNotExpiredProjectClick Коллбек, вызываемый при нажатии на сам проект, если он активен.
 * @param onExpiredProjectClick Коллбек, вызываемый при нажатии на сам проект.
 * @param onProjectInfoClick Коллбек, срабатывающий при клике на иконку информации о проекте.
 */
@Composable
internal fun ProjectItem(
    project: Project,
    modifier: Modifier = Modifier,
    expired: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = CodeTogetherTheme.colors.primary.copy(alpha = 0.015f),
    onIsNotExpiredProjectClick: () -> Unit,
    onExpiredProjectClick: () -> Unit,
    onProjectInfoClick: (Int) -> Unit,
) {
    val contentColor = if (expired) CodeTogetherTheme.colors.error.copy(alpha = 0.15f) else Color.White
    val backgroundColor =
        if (expired) CodeTogetherTheme.colors.error.copy(alpha = 0.15f) else backgroundColor
    Surface(
        onClick = {

            if (expired) onExpiredProjectClick() else onIsNotExpiredProjectClick()
        },
        modifier = modifier,
        shape = shape,
        contentColor = contentColor,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CodeTogetherText(
                text = project.name,
                color = CodeTogetherTheme.colors.primaryText,
                style = CodeTogetherTheme.typography.style.copy(fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.weight(1f))
            CodeTogetherIconButton(
                onClick = {
                    if (expired) onExpiredProjectClick() else onProjectInfoClick(project.id)
                }
            ) {
                Icon(
                    painter = painterResource(AppIcons.ArrowRightIn),
                    contentDescription = "Project info",
                    tint = CodeTogetherTheme.colors.primary
                )
            }
        }
    }
}

@Composable
@Preview
private fun ProjectItemPreview() {
    BackgroundPreview {
        ProjectItem(
            project = Project(id = 1, name = "Hello World"),
            onIsNotExpiredProjectClick = {

            },
            onProjectInfoClick = {

            },
            onExpiredProjectClick = {

            }
        )
    }
}