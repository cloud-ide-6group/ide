package ru.vsu.front.profile.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Кнопка выбора языка программирования.
 *
 * @param programingLanguage Объект языка программирования.
 * @param modifier Модификатор для настройки.
 * @param icon Локальный ресурс иконки. `null`, если иконка не найдена.
 * @param onClick Коллбек, возвращающий выбранный язык при нажатии.
 */
@Composable
internal fun LanguageButtonItem(
    programingLanguage: ProgramingLanguage,
    modifier: Modifier = Modifier,
    icon: DrawableResource?,
    onClick: (ProgramingLanguage) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        icon?.let {
            Icon(
                painter = painterResource(icon),
                modifier = Modifier.align(Alignment.CenterStart),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        CodeTogetherTextButton(
            text = programingLanguage.name,
            modifier = Modifier.fillMaxWidth(),
            textColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            onClick = {
                onClick(programingLanguage)
            }
        )
    }
}