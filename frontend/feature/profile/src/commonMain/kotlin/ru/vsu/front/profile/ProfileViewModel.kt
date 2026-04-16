package ru.vsu.front.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.GetProfileUseCase
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Project
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User

/**
 * Вьюмодель экрана профиля.
 *
 * @param userId Айди пользователя.
 * @param getProfileUseCase UseCase для получения профиля.
 * @param dispatcherProvider Провайдер корутинных диспетчеров.
 */
class ProfileViewModel(
    private val userId: Int,
    private val getProfileUseCase: GetProfileUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiStatusProfile>(UiStatusProfile.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadUserInfo(userId)
    }

    fun processCommand(command: ProfileCommand) {
        when (command) {
            is ProfileCommand.ChangeName ->
                updateLoadedState { it.copy(name = command.name) }

            is ProfileCommand.ChangeEmail ->
                updateLoadedState { it.copy(email = command.email) }

            is ProfileCommand.ChangeCurrentPassword ->
                updateLoadedState { it.copy(currentPassword = command.currentPassword) }

            is ProfileCommand.ChangeNewPassword ->
                updateLoadedState { it.copy(newPassword = command.newPassword) }

            ProfileCommand.ChangeCurrentPasswordVisibility ->
                updateLoadedState { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }

            ProfileCommand.ChangeNewPasswordVisibility ->
                updateLoadedState { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }

            ProfileCommand.ChangeProjectsVisibility ->
                updateLoadedState { it.copy(areProjectsVisible = !it.areProjectsVisible) }

            is ProfileCommand.ChangeProgramingLanguage ->
                updateLoadedState { it.copy(projectProgramingLanguage = command.programingLanguage) }

            is ProfileCommand.ChangeProjectName ->
                updateLoadedState { it.copy(projectName = command.projectName) }
        }
    }

    private fun loadUserInfo(userId: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            val result = getProfileUseCase(userId)
            handleAuthResult(result)
        }
    }

    private fun handleAuthResult(result: Response<User>) {
        when (result) {
            is Response.Error<*> -> {
                println(result.requestError.message)
            }

            is Response.Success<User> -> {
                val user = result.data
                _uiState.update {
                    UiStatusProfile.Loaded(
                        uiStateProfileLoaded = UiStateProfileLoaded(
                            name = user.name,
                            email = user.email,
                            photo = user.photo,
                            projects = user.projects,
                        )
                    )
                }
            }
        }
    }

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
    data class ChangeProjectName(val projectName: String) : ProfileCommand
    data class ChangeProgramingLanguage(val programingLanguage: ProgramingLanguage) : ProfileCommand
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
    val projectProgramingLanguage: ProgramingLanguage = defaultProgramingLanguages.first(),
    val projectLanguages: List<ProgramingLanguage> = defaultProgramingLanguages,
    val isProjectsListExpanded: Boolean = false
)

/**
 * Текущий статус экрана.
 *
 * [Loading] - Данные загружаются.
 * [Loaded] - Данные загружены.
 */
sealed interface UiStatusProfile {
    data object Loading : UiStatusProfile
    data class Loaded(val uiStateProfileLoaded: UiStateProfileLoaded) : UiStatusProfile
}

// Тестовые данные
private val defaultProgramingLanguages = buildList {
    add(
        ProgramingLanguage(
            id = 1,
            name = "Python",
            description = "",
            imageName = "python"
        )
    )
    add(
        ProgramingLanguage(
            id = 2,
            name = "Empty Python",
            description = "",
            imageName = "python"
        )
    )
    add(
        ProgramingLanguage(
            id = 3,
            name = "JavaScript",
            description = "",
            imageName = "JavaScript"
        )
    )
    add(
        ProgramingLanguage(
            id = 4,
            name = "Java 21",
            description = "",
            imageName = "Java-21"
        )
    )
    add(
        ProgramingLanguage(
            id = 5,
            name = "Java 17",
            description = "",
            imageName = "Java-17"
        )
    )
    add(
        ProgramingLanguage(
            id = 6,
            name = "Lua",
            description = "",
            imageName = "Lua"
        )
    )
}