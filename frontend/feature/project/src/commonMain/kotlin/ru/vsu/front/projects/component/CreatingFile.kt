package ru.vsu.front.projects.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.AppIcons
import ru.vsu.front.designsystem.component.CodeTogetherIconButton
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.CodeTogetherTextField
import ru.vsu.front.designsystem.theme.CodeTogetherTheme

/**
 * Диалоговое окно для создания нового файла или папки в проекте.
 *
 * @param modifier Модификатор для настройки.
 * @param fileName Название файла.
 * @param parentId Идентификатор родительской папки (или null, если элемент создается в корне).
 * @param onCreateClick Коллбек, вызываемый при подтверждении создания элемента.
 * @param onValueChange Коллбек для обновления состояния вводимого имени.
 * @param isFolderSelected Флаг, указывающий, что пользователь выбрал создание папки, а не файла.
 * @param onClickSelectFile Коллбек, срабатывающий при переключении режима на создание файла.
 * @param onClickSelectFolder Коллбек, срабатывающий при переключении режима на создание папки.
 */
@Composable
fun CreatingFile(
    modifier: Modifier = Modifier,
    fileName: String,
    parentId: Int?,
    onCreateClick: (Int?) -> Unit,
    onValueChange: (String) -> Unit,
    isFolderSelected: Boolean,
    onClickSelectFile: () -> Unit,
    onClickSelectFolder: () -> Unit,
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
                text = "Creating file"
            )
            CodeTogetherTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = fileName,
                onValueChange = onValueChange,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val selectedTint = CodeTogetherTheme.colors.primary
                val unselectedTint = CodeTogetherTheme.colors.primaryText
                CodeTogetherIconButton(
                    onClick = onClickSelectFile,
                ) {
                    Icon(
                        painter = painterResource(AppIcons.File),
                        contentDescription = "Select file",
                        tint = if (!isFolderSelected) selectedTint else unselectedTint
                    )
                }
                CodeTogetherIconButton(
                    onClick = onClickSelectFolder
                ) {
                    Icon(
                        painter = painterResource(AppIcons.Folder),
                        contentDescription = "Select folder",
                        tint = if (isFolderSelected) selectedTint else unselectedTint
                    )
                }
            }
            CodeTogetherTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Create",
                textColor = CodeTogetherTheme.colors.primary,
                onClick = {
                    onCreateClick(parentId)
                }
            )
        }
    }
}