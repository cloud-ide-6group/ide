package ru.vsu.front.notifications

import androidx.compose.runtime.Composable
import ru.vsu.front.designsystem.component.CodeTogetherScaffold
import ru.vsu.front.designsystem.component.CodeTogetherText

@Composable
fun ProjectScreen(
    viewModel: ProjectViewModel,
) {
    CodeTogetherScaffold {
        CodeTogetherText(text = "ProjectScreen")
    }
}