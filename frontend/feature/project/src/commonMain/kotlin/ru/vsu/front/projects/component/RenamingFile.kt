package ru.vsu.front.projects.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.CodeTogetherTextField
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Диалоговое окно для ввода нового имени файла или папки.
 *
 * @param modifier Модификатор для настройки компонента.
 * @param fileName Текущее введенное имя файла, отображаемое в текстовом поле.
 * @param fileId Уникальный идентификатор переименовываемого элемента.
 * @param onValueChange Коллбек, вызываемый при каждом изменении текста в поле ввода.
 * @param onRenameClick Коллбек, срабатывающий при нажатии на кнопку "Rename", передает ID и новое имя.
 */
@Composable
fun RenamingFile(
    modifier: Modifier = Modifier,
    fileName: String,
    fileId: Int,
    onValueChange: (String) -> Unit,
    onRenameClick: (Int, String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CodeTogetherTheme.colors.secondaryBackground,
        tonalElevation = 8.dp,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { }
    ) {
        Column(
            modifier = Modifier
                .width(480.dp)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CodeTogetherText(
                text = "Renaming file"
            )
            CodeTogetherTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = fileName,
                onValueChange = onValueChange,
            )
            CodeTogetherTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Rename",
                textColor = CodeTogetherTheme.colors.primary,
                onClick = {
                    onRenameClick(fileId, fileName)
                }
            )
        }
    }
}