package ru.vsu.front.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import ru.vsu.front.designsystem.common.localIconRes
import ru.vsu.front.designsystem.component.CodeTogetherText
import ru.vsu.front.designsystem.component.CodeTogetherTextButton
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.model.entity.ProgramingLanguage

@Composable
internal fun CreatingProject(
    projectName: String,
    selectedProgramingLanguage: ProgramingLanguage,
    programingLanguagesListExpanded: Boolean,
    programingLanguages: List<ProgramingLanguage>,
    modifier: Modifier = Modifier,
    onProjectNameChange: (String) -> Unit,
    onProgramingLanguageClick: (ProgramingLanguage) -> Unit,
    onSelectedProgramingLanguageClick: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onDismissRequest: () -> Unit,
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
        var columnWidthPx by remember { mutableIntStateOf(0) }
        val density = LocalDensity.current

        Column(
            modifier = Modifier
                .width(640.dp)
                .padding(32.dp)
                .onGloballyPositioned { coordinates ->
                    columnWidthPx = coordinates.size.width
                },
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                CodeTogetherText(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Creating Project",
                    color = Color.White
                )
                selectedProgramingLanguage.localIconRes?.let {
                    Icon(
                        modifier = Modifier.align(Alignment.TopEnd),
                        painter = painterResource(it),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }

            Section(
                sectionName = "Project Name",
                value = projectName,
                hint = "Enter Project Name",
                onValueChange = onProjectNameChange
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                LanguageButtonItem(
                    programingLanguage = selectedProgramingLanguage,
                    icon = null,
                    onClick = {
                        onSelectedProgramingLanguageClick()
                    }
                )

                DropdownMenu(
                    modifier = Modifier
                        .width(with(density) { columnWidthPx.toDp() })
                        .background(CodeTogetherTheme.colors.primaryBackground)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    expanded = programingLanguagesListExpanded,
                    onDismissRequest = onDismissRequest
                ) {
                    LanguageButtonItems(
                        modifier = Modifier.heightIn(max = 384.dp),
                        languages = programingLanguages,
                        onLanguageClick = {
                            onProgramingLanguageClick(it)
                        }
                    )
                }
            }

            CodeTogetherTextButton(
                text = "Create",
                modifier = Modifier.fillMaxWidth(),
                textColor = CodeTogetherTheme.colors.primary
            ) {
                onCreateProjectClick()
            }
        }
    }
}