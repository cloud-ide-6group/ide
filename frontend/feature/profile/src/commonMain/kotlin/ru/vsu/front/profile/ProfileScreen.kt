package ru.vsu.front.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.ismoy.imagepickerkmp.domain.config.GalleryConfig
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBase64
import io.github.ismoy.imagepickerkmp.features.imagepicker.config.ImagePickerKMPConfig
import io.github.ismoy.imagepickerkmp.features.imagepicker.model.ImagePickerResult
import io.github.ismoy.imagepickerkmp.features.imagepicker.ui.rememberImagePickerKMP
import ru.vsu.front.designsystem.component.*
import ru.vsu.front.designsystem.theme.CodeTogetherTheme
import ru.vsu.front.profile.component.*

/**
 * Экран профиля.
 *
 * @param viewModel Вьюмодель для экрана профиля.
 * @param modifier Модификатор для настройки.
 * @param onProjectInfoClick Коллбек, вызываемый при клике на проект.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    onProjectInfoClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val picker = rememberImagePickerKMP(
        config = ImagePickerKMPConfig(
            galleryConfig = GalleryConfig(
                allowMultiple = false,
                selectionLimit = 1
            )
        )
    )

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ProfileEffect.ShowMessage -> snackbarHostState.showSnackbar(message = event.message)
            }
        }
    }

    when (val result = picker.result) {
        is ImagePickerResult.Success -> {
            val photo = result.first
            photo?.let {
                viewModel.processCommand(ProfileCommand.UpdatePhoto(photo.loadBase64()))
            }
        }

        else -> {
        }
    }

    CodeTogetherScaffold(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
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
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        UserAvatar(
                            modifier = Modifier.size(348.dp),
                            photoBase64 = loadedState.photo,
                            onClick = {
                                picker.launchGallery()
                            }
                        )
                        ProfileSections(
                            modifier = Modifier
                                .weight(1f),
                            name = loadedState.name,
                            email = loadedState.email,
                            currentPassword = loadedState.currentPassword,
                            newPassword = loadedState.newPassword,
                            hasChangesName = loadedState.hasChangesName,
                            hasChangesEmail = loadedState.emailIsReadyForChange,
                            hasChangesPassword = loadedState.hasChangesNewPassword,
                            isCurrentPasswordVisible = loadedState.isCurrentPasswordVisible,
                            isNewPasswordVisible = loadedState.isNewPasswordVisible,
                            onNameChange = {
                                viewModel.processCommand(ProfileCommand.ChangeName(it))
                            },
                            onEmailChange = {
                                viewModel.processCommand(ProfileCommand.ChangeEmail(it))
                            },
                            onCurrentPasswordChange = {
                                viewModel.processCommand(ProfileCommand.ChangeCurrentPassword(it))
                            },
                            onNewPasswordChange = {
                                viewModel.processCommand(ProfileCommand.ChangeNewPassword(it))
                            },
                            onCurrentPasswordVisibilityChange = {
                                viewModel.processCommand(ProfileCommand.ChangeCurrentPasswordVisibility)
                            },
                            onNewPasswordVisibilityChange = {
                                viewModel.processCommand(ProfileCommand.ChangeNewPasswordVisibility)
                            },
                            onUpdateDataClick = {
                                viewModel.processCommand(ProfileCommand.UpdateData)
                            },
                            onUpdatePasswordClick = {
                                viewModel.processCommand(ProfileCommand.UpdatePassword)
                            },
                        )
                    }

                    ProjectsSection(
                        modifier = Modifier.weight(1f),
                        projects = loadedState.projects,
                        projectsAreVisible = loadedState.areProjectsVisible,
                        onChangeVisibleClick = {
                            viewModel.processCommand(ProfileCommand.ChangeProjectsVisibility)
                        },
                        onCreateProjectClick = {
                            viewModel.processCommand(ProfileCommand.ChangeCreateProjectDialogVisibility)
                        },
                        onProjectClick = { projectId ->
                            onProjectInfoClick(projectId)
                        }
                    )
                }

                CustomDialog(
                    show = loadedState.isCreateProjectDialogShown,
                    onDismissRequest = {
                        viewModel.processCommand(ProfileCommand.ChangeCreateProjectDialogVisibility)
                    }
                ) {
                    CreatingProject(
                        projectName = loadedState.projectName,
                        selectedProgramingLanguage = loadedState.selectedProgramingLanguageForProject,
                        programingLanguagesListExpanded = loadedState.isProgramingLanguagesExpanded,
                        programingLanguages = loadedState.programingLanguages,
                        onProjectNameChange = {
                            viewModel.processCommand(ProfileCommand.ChangeProjectName(it))
                        },
                        onProgramingLanguageClick = {
                            viewModel.processCommand(ProfileCommand.ChangeProgramingLanguage(it))
                            viewModel.processCommand(ProfileCommand.ChangeProgramingLanguagesVisibility)
                        },
                        onSelectedProgramingLanguageClick = {
                            viewModel.processCommand(ProfileCommand.ChangeProgramingLanguagesVisibility)
                        },
                        onCreateProjectClick = {
                            viewModel.processCommand(ProfileCommand.CreateProject)
                        },
                        onDismissRequest = {
                            viewModel.processCommand(ProfileCommand.ChangeProgramingLanguagesVisibility)
                        }
                    )
                }
            }

            UiStatusProfile.Loading -> {
                LoadingScreen()
            }

            UiStatusProfile.Error -> {
                ErrorScreen(
                    onClick = {
                        viewModel.processCommand(ProfileCommand.RepeatLoadingProfile)
                    }
                )
            }
        }
    }
}