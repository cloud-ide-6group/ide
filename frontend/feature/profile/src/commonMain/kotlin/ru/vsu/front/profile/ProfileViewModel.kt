package ru.vsu.front.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.*
import ru.vsu.front.domain.validation.EmailMatcher
import ru.vsu.front.model.entity.*

/**
 * Вьюмодель экрана профиля.
 *
 * @param getProfileUseCase UseCase для получения профиля.
 * @param getProgramingLanguagesUseCase UseCase получения доступных языков программирования.
 * @param createProjectUseCase UseCase для создания проекта.
 * @param updateProfileDataUseCase UseCase для обновления почты и имени пользователя.
 * @param updateProfilePasswordUseCase UseCase для обновления пароля.
 * @param createProjectUseCase UseCase для создания проекта.
 * @param updateProfilePhotoUseCase UseCase для обновления аватара пользователя.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
 */
class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getProgramingLanguagesUseCase: GetProgramingLanguagesUseCase,
    private val createProjectUseCase: CreateProjectUseCase,
    private val updateProfileDataUseCase: UpdateProfileDataUseCase,
    private val updateProfilePasswordUseCase: UpdateProfilePasswordUseCase,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiStatusProfile>(UiStatusProfile.Loading)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ProfileEffect>()
    val events: SharedFlow<ProfileEffect>
        get() = _events.asSharedFlow()

    init {
        loadProfile()
    }

    /**
     * Выполняет определенные действия в зависимости от переданной команды.
     */
    fun processCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.ChangeName -> {
                updateLoadedState {
                    val hasChangesName = it.initialName.trim() != command.name.trim()
                    it.copy(
                        name = command.name,
                        hasChangesName = hasChangesName,
                    )
                }
            }

            is ProfileCommand.ChangeEmail -> {
                updateLoadedState {
                    val trimmedEmail = command.email.trim()
                    it.copy(
                        email = command.email,
                        emailIsReadyForChange = it.initialEmail.trim() != trimmedEmail && EmailMatcher.isValid(
                            trimmedEmail
                        ),
                    )
                }
            }

            is ProfileCommand.ChangeCurrentPassword -> {
                updateLoadedState { it.copy(currentPassword = command.currentPassword) }
            }

            is ProfileCommand.ChangeNewPassword -> {
                updateLoadedState {
                    it.copy(
                        newPassword = command.newPassword,
                        hasChangesNewPassword = command.newPassword.trim() != ""
                    )
                }
            }

            ProfileCommand.ChangeCurrentPasswordVisibility -> {
                updateLoadedState { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }
            }

            ProfileCommand.ChangeNewPasswordVisibility -> {
                updateLoadedState { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
            }

            ProfileCommand.ChangeProjectsVisibility -> {
                updateLoadedState { it.copy(areProjectsVisible = !it.areProjectsVisible) }
            }

            is ProfileCommand.ChangeProgramingLanguage -> {
                updateLoadedState { it.copy(selectedProgramingLanguageForProject = command.programingLanguage) }
            }

            is ProfileCommand.ChangeProjectName -> {
                updateLoadedState { it.copy(projectName = command.projectName) }
            }

            ProfileCommand.CreateProject -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val currentState =
                        (_uiState.value as? UiStatusProfile.Loaded)?.uiStateProfileLoaded ?: return@launch

                    val languageId = currentState.selectedProgramingLanguageForProject?.id ?: return@launch
                    val trimmedProjectName = currentState.projectName.trim()

                    if (trimmedProjectName.isEmpty()) return@launch

                    val result = createProjectUseCase(languageId, trimmedProjectName)

                    updateLoadedState { latestState ->
                        when (result) {
                            is Response.Success<Int> -> {
                                _events.emit(ProfileEffect.ShowMessage("Проект создан"))
                                latestState.copy(
                                    projects = latestState.projects + Project(
                                        id = result.data,
                                        name = trimmedProjectName
                                    ),
                                    projectName = "",
                                    selectedProgramingLanguageForProject = latestState.programingLanguages.first(),
                                    isCreateProjectDialogShown = false
                                )
                            }

                            is Response.Error<*> -> {
                                when (val requestError = result.requestError) {
                                    is RequestError.Conflict -> {
                                        _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                    }

                                    is RequestError.Forbidden -> {
                                        _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                    }

                                    is RequestError.UnknownError -> {
                                        _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                    }

                                    is RequestError.NetworkException -> {
                                        _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                    }

                                    else -> {
                                    }
                                }
                                latestState
                            }
                        }
                    }
                }
            }

            ProfileCommand.ChangeCreateProjectDialogVisibility -> {
                updateLoadedState {
                    it.copy(isCreateProjectDialogShown = !it.isCreateProjectDialogShown)
                }
            }

            ProfileCommand.ChangeProgramingLanguagesVisibility -> {
                updateLoadedState {
                    it.copy(isProgramingLanguagesExpanded = !it.isProgramingLanguagesExpanded)
                }
            }

            ProfileCommand.RepeatLoadingProfile -> {
                loadProfile()
            }

            ProfileCommand.UpdateData -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val currentState =
                        (_uiState.value as? UiStatusProfile.Loaded)?.uiStateProfileLoaded ?: return@launch

                    val email = currentState.email.trim()
                    val name = currentState.name.trim()

                    when (val result = updateProfileDataUseCase(email = email, name = name)) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Unauthorized -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                is RequestError.NotFound -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                is RequestError.NetworkException -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                is RequestError.UnknownError -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _events.emit(ProfileEffect.ShowMessage("Данные обновлены"))
                            updateLoadedState {
                                it.copy(
                                    initialEmail = email,
                                    initialName = name,
                                    emailIsReadyForChange = email != it.email,
                                    hasChangesName = name != it.name,
                                )
                            }
                        }
                    }
                }
            }

            ProfileCommand.UpdatePassword -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    val currentState =
                        (_uiState.value as? UiStatusProfile.Loaded)?.uiStateProfileLoaded ?: return@launch

                    val currentPassword = currentState.currentPassword.trim()
                    val newPassword = currentState.newPassword.trim()

                    when (val result = updateProfilePasswordUseCase(
                        oldPassword = currentPassword,
                        newPassword = newPassword
                    )) {
                        is Response.Error<*> -> {
                            when (val requestError = result.requestError) {
                                is RequestError.Conflict -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                is RequestError.Unauthorized -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                is RequestError.UnknownError -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                is RequestError.NetworkException -> {
                                    _events.emit(ProfileEffect.ShowMessage(requestError.message))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            _events.emit(ProfileEffect.ShowMessage("Данные обновлены"))
                            updateLoadedState {
                                it.copy(
                                    currentPassword = "",
                                    newPassword = "",
                                    hasChangesNewPassword = newPassword != it.newPassword,
                                )
                            }
                        }
                    }
                }
            }

            is ProfileCommand.UpdatePhoto -> {
                viewModelScope.launch(dispatcherProvider.io) {
                    when (val result = updateProfilePhotoUseCase(
                        photoBase64 = command.photoBase64
                    )) {
                        is Response.Error<*> -> {
                            when (result.requestError) {
                                is RequestError.Unauthorized -> {
                                    _events.emit(ProfileEffect.ShowMessage(result.requestError.message))
                                }

                                is RequestError.Conflict -> {
                                    _events.emit(ProfileEffect.ShowMessage(result.requestError.message))
                                }

                                is RequestError.UnknownError -> {
                                    _events.emit(ProfileEffect.ShowMessage(result.requestError.message))
                                }

                                is RequestError.NetworkException -> {
                                    _events.emit(ProfileEffect.ShowMessage(result.requestError.message))
                                }

                                else -> {
                                }
                            }
                        }

                        is Response.Success<*> -> {
                            updateLoadedState {
                                it.copy(photo = command.photoBase64)
                            }
                            _events.emit(ProfileEffect.ShowMessage("Аватар изменён"))
                        }
                    }
                }
            }
        }
    }

    /**
     * Загрузка профиля.
     */
    private fun loadProfile() {
        viewModelScope.launch(dispatcherProvider.io) {
            _uiState.update {
                UiStatusProfile.Loading
            }
            val profileResponse = async {
                getProfileUseCase()
            }

            val programingLanguagesResponse = async {
                getProgramingLanguagesUseCase()
            }

            when (val profile = profileResponse.await()) {
                is Response.Success<UserProfile> -> {
                    when (val programingLanguages = programingLanguagesResponse.await()) {
                        is Response.Error<*> -> {
                            _uiState.update {
                                UiStatusProfile.Error
                            }
                        }

                        is Response.Success<List<ProgramingLanguage>> -> {
                            _uiState.update {
                                UiStatusProfile.Loaded(
                                    uiStateProfileLoaded = UiStateProfileLoaded(
                                        name = profile.data.name,
                                        email = profile.data.email,
                                        initialName = profile.data.name,
                                        initialEmail = profile.data.email,
                                        photo = profile.data.photo,
                                        projects = profile.data.projects,
                                        selectedProgramingLanguageForProject = programingLanguages.data.first(),
                                        programingLanguages = programingLanguages.data
                                    )
                                )
                            }
                        }
                    }
                }

                is Response.Error<*> -> {
                    _uiState.update {
                        UiStatusProfile.Error
                    }
                }
            }
        }
    }

    /**
     * Обновление состояния, если оно текущее состояние Loaded.
     */
    private inline fun updateLoadedState(transform: (UiStateProfileLoaded) -> UiStateProfileLoaded) {
        _uiState.update { currentState ->
            if (currentState is UiStatusProfile.Loaded) {
                UiStatusProfile.Loaded(transform(currentState.uiStateProfileLoaded))
            } else {
                currentState
            }
        }
    }
}

/**
 * Команды.
 */
sealed interface ProfileCommand {
    data class ChangeName(val name: String) : ProfileCommand
    data class ChangeEmail(val email: String) : ProfileCommand
    data class ChangeCurrentPassword(val currentPassword: String) : ProfileCommand
    data class ChangeNewPassword(val newPassword: String) : ProfileCommand
    data object ChangeCurrentPasswordVisibility : ProfileCommand
    data object ChangeNewPasswordVisibility : ProfileCommand
    data object ChangeProjectsVisibility : ProfileCommand
    data object ChangeProgramingLanguagesVisibility : ProfileCommand
    data object ChangeCreateProjectDialogVisibility : ProfileCommand
    data class ChangeProjectName(val projectName: String) : ProfileCommand
    data class ChangeProgramingLanguage(val programingLanguage: ProgramingLanguage) : ProfileCommand
    data object CreateProject : ProfileCommand
    data object UpdateData : ProfileCommand
    data object UpdatePassword : ProfileCommand
    data class UpdatePhoto(val photoBase64: String) : ProfileCommand
    data object RepeatLoadingProfile : ProfileCommand
}

/**
 * Состояние экрана.
 */
data class UiStateProfileLoaded(
    val name: String = "",
    val email: String = "",
    val photo: String = "",
    val projects: List<Project> = emptyList(),
    val currentPassword: String = "",
    val newPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val areProjectsVisible: Boolean = true,
    val projectName: String = "",
    val selectedProgramingLanguageForProject: ProgramingLanguage? = null,
    val programingLanguages: List<ProgramingLanguage> = emptyList(),
    val isProgramingLanguagesExpanded: Boolean = false,
    val isCreateProjectDialogShown: Boolean = false,
    val initialEmail: String = "",
    val initialName: String = "",
    val emailIsReadyForChange: Boolean = false,
    val hasChangesName: Boolean = false,
    val hasChangesNewPassword: Boolean = false,
)

/**
 * Текущий статус экрана.
 */
sealed interface UiStatusProfile {
    /**
     * Данные загружаются.
     */
    data object Loading : UiStatusProfile

    /**
     * Данные загружены.
     */
    data class Loaded(val uiStateProfileLoaded: UiStateProfileLoaded) : UiStatusProfile

    /**
     * Ошибка при загрузке профиля.
     */
    data object Error : UiStatusProfile
}

/**
 * События экрана профиля.
 */
sealed interface ProfileEffect {
    /**
     * Показывает сообщение на экране.
     */
    data class ShowMessage(val message: String) : ProfileEffect
}
