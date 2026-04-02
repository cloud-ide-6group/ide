package ru.vsu.front.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vsu.front.designsystem.component.CodeTogetherScaffold
import ru.vsu.front.designsystem.component.Section
import ru.vsu.front.designsystem.component.VisibilityButton
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.profile.component.CreatingProject
import ru.vsu.front.profile.component.CustomDialog
import ru.vsu.front.profile.component.ProjectsSection
import ru.vsu.front.profile.component.UserAvatar

/**
 * Экран профиля.
 *
 * @param viewModel Вьюмодель для экрана профиля.
 * @param modifier Модификатор для настройки.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
) {
    var createProjectDialogIsShown by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CodeTogetherScaffold(
        modifier = modifier,
        backgroundColor = CodeTogetherTheme.colors.primaryBackground
    ) {
        when (val currentState = uiState) {
            is UiStatusProfile.Loaded -> {
                val loadedState = currentState.uiStateProfileLoaded
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        UserAvatar(
                            photoPath = loadedState.photoPath,
                            onClick = {

                            }
                        )
                        Column(
                            modifier = Modifier.padding(horizontal = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Section(
                                sectionName = "Name",
                                value = loadedState.name,
                                hint = "Name",
                                onValueChange = {
                                    viewModel.processCommand(ProfileCommand.ChangeName(it))
                                }
                            )

                            Section(
                                sectionName = "Email",
                                value = loadedState.email,
                                hint = "Email",
                                onValueChange = {
                                    viewModel.processCommand(ProfileCommand.ChangeEmail(it))
                                }
                            )

                            Section(
                                sectionName = "Current Password",
                                value = loadedState.currentPassword,
                                hint = "Your Current Password",
                                onValueChange = {
                                    viewModel.processCommand(ProfileCommand.ChangeCurrentPassword(it))
                                },
                                visualTransformation = if (loadedState.isCurrentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    VisibilityButton(
                                        isVisible = loadedState.isCurrentPasswordVisible,
                                        onClick = {
                                            viewModel.processCommand(ProfileCommand.ChangeCurrentPasswordVisibility)
                                        }
                                    )
                                }
                            )

                            Section(
                                sectionName = "New Password",
                                value = loadedState.newPassword,
                                hint = "Your New Password",
                                onValueChange = {
                                    viewModel.processCommand(ProfileCommand.ChangeNewPassword(it))
                                },
                                visualTransformation = if (loadedState.isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    VisibilityButton(
                                        isVisible = loadedState.isNewPasswordVisible,
                                        onClick = {
                                            viewModel.processCommand(ProfileCommand.ChangeNewPasswordVisibility)
                                        }
                                    )
                                }
                            )
                        }
                    }

                    ProjectsSection(
                        projects = loadedState.projects,
                        projectsAreVisible = loadedState.projectsAreVisible,
                        onChangeVisibleClick = {
                            viewModel.processCommand(ProfileCommand.ChangeProjectsVisibility)
                        },
                        onCreateProjectClick = {
                            createProjectDialogIsShown = true
                        },
                        onProjectClick = {
                            // TODO NAVIGATE
                        }
                    )
                }

                var programingLanguagesListExpanded by remember { mutableStateOf(false) }
                CustomDialog(
                    show = createProjectDialogIsShown,
                    onDismissRequest = { createProjectDialogIsShown = false }
                ) {
                    CreatingProject(
                        projectName = loadedState.projectName,
                        selectedProgramingLanguage = loadedState.projectProgramingLanguage,
                        programingLanguagesListExpanded = programingLanguagesListExpanded,
                        programingLanguages = loadedState.projectLanguages,
                        onProjectNameChange = {
                            viewModel.processCommand(ProfileCommand.ChangeProjectName(it))
                        },
                        onProgramingLanguageClick = {
                            viewModel.processCommand(ProfileCommand.ChangeProgramingLanguage(it))
                            programingLanguagesListExpanded = false
                        },
                        onSelectedProgramingLanguageClick = {
                            programingLanguagesListExpanded = true
                        },
                        onCreateProjectClick = {
                            createProjectDialogIsShown = false
                        },
                        onDismissRequest = {
                            programingLanguagesListExpanded = false
                        }
                    )
                }
            }

            UiStatusProfile.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CodeTogetherTheme.colors.primary)
                }
            }
        }
    }
}