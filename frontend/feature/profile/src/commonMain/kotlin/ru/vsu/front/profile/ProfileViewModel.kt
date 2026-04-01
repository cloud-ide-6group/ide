package ru.vsu.front.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vsu.front.domain.usecase.GetProfileUseCase
import ru.vsu.front.model.entity.Project
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User
import java.time.temporal.TemporalAdjusters.previous

/**
 * Вьюмодель экрана авторизации и регистрации.
 *
 * @param userId Айди пользователя.
 * @param getProfileUseCase UseCase для получения профиля.
 */
class ProfileViewModel(
    private val userId: Int,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiStatusProfile>(UiStatusProfile.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadProfileInfo(userId)
        }
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
                updateLoadedState { it.copy(projectsAreVisible = !it.projectsAreVisible) }
        }
    }

    private suspend fun loadProfileInfo(userId: Int) {
        _uiState.update {
            when (val profile = getProfileUseCase(userId)) {
                is Response.Error<*> -> TODO()
                is Response.Success<User> -> {
                    val profileInstance = profile.data
                    UiStatusProfile.Loaded(
                        uiStateProfileLoaded = UiStateProfileLoaded(
                            name = profileInstance.name,
                            email = profileInstance.email,
                            photoPath = profileInstance.photoPath,
                            projects = profileInstance.projects,
                            currentPassword = "",
                            newPassword = "",
                            isCurrentPasswordVisible = false,
                            isNewPasswordVisible = false,
                            projectsAreVisible = true
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
sealed interface ProfileCommand {
    data class ChangeName(val name: String) : ProfileCommand
    data class ChangeEmail(val email: String) : ProfileCommand
    data class ChangeCurrentPassword(val currentPassword: String) : ProfileCommand
    data class ChangeNewPassword(val newPassword: String) : ProfileCommand
    data object ChangeCurrentPasswordVisibility : ProfileCommand
    data object ChangeNewPasswordVisibility : ProfileCommand
    data object ChangeProjectsVisibility : ProfileCommand
}
data class UiStateProfileLoaded(
    val name: String,
    val email: String,
    val photoPath: String,
    val projects: List<Project>,
    val currentPassword: String,
    val newPassword: String,
    val isCurrentPasswordVisible: Boolean,
    val isNewPasswordVisible: Boolean,
    val projectsAreVisible: Boolean
)

sealed interface UiStatusProfile {
    data object Loading : UiStatusProfile
    data class Loaded(val uiStateProfileLoaded: UiStateProfileLoaded) : UiStatusProfile
}