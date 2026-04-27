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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import front.feature.profile.generated.resources.Res
import front.feature.profile.generated.resources.add_2_24dp
import front.feature.profile.generated.resources.code_horizontal_24dp
import front.feature.profile.generated.resources.code_vertical_24dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Контейнер с кнопками добавления проекта и изменения видимости проектов.
 *
 * @param projectsAreVisible Видимость списка проектов.
 * @param modifier Модификатор ля настройки.
 * @param shape Форма контейнера.
 * @param onCreateProjectClick Коллбек для кнопки добавления нового проекта.
 * @param onChangeVisibleClick Коллбек для кнопки изменения видимости списка.
 */
@Composable
internal fun ProjectsButton(
    projectsAreVisible: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    onCreateProjectClick: () -> Unit,
    onChangeVisibleClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .fillMaxWidth(),
        contentColor = Color.White,
        color = CodeTogetherTheme.colors.primary.copy(alpha = 0.025f)
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CodeTogetherText(
                text = "Projects",
                color = CodeTogetherTheme.colors.primaryText,
                style = CodeTogetherTheme.typography.style.copy(fontSize = 16.sp)
            )

            Spacer(Modifier.weight(1f))

            CodeTogetherIconButton(
                onClick = onCreateProjectClick,
                hoverColor = Color.White.copy(alpha = 0.0025f)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_2_24dp),
                    contentDescription = null,
                    tint = CodeTogetherTheme.colors.primary,
                )
            }

            CodeTogetherIconButton(
                onClick = onChangeVisibleClick,
                hoverColor = Color.White.copy(alpha = 0.0025f)
            ) {
                Icon(
                    painter = painterResource(
                        if (projectsAreVisible) Res.drawable.code_vertical_24dp else Res.drawable.code_horizontal_24dp
                    ),
                    contentDescription = null,
                    tint = CodeTogetherTheme.colors.primary,
                )
            }
        }
    }
}


