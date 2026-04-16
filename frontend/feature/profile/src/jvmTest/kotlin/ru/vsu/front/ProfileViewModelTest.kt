package ru.vsu.front

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.GetProfileUseCase
import ru.vsu.front.domain.usecase.LoginUseCase
import ru.vsu.front.domain.usecase.SignUseCase
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User
import ru.vsu.front.profile.ProfileCommand
import ru.vsu.front.profile.ProfileViewModel
import ru.vsu.front.profile.UiStatusProfile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Тесты для [ProfileViewModelTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val getProfileUseCase = mockk<GetProfileUseCase>()
    private val testDispatcher = StandardTestDispatcher()
    
    private val testUserId = 1
    private val mockUser = User(
        name = "name",
        email = "mail@mail.ru",
        photo = "base64",
        projects = emptyList()
    )

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getProfileUseCase(testUserId) } returns Response.Success(mockUser)

        viewModel = ProfileViewModel(
            userId = testUserId,
            getProfileUseCase = getProfileUseCase,
            dispatcherProvider = object : DispatcherProvider {
                override val main = testDispatcher
                override val io = testDispatcher
                override val default = testDispatcher
            }
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized, should load user data and emit Loaded state`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            
            if (state is UiStatusProfile.Loading) {
                val loadedState = awaitItem()
                assertIs<UiStatusProfile.Loaded>(loadedState)
                assertEquals(mockUser.name, loadedState.uiStateProfileLoaded.name)
            } else {
                assertIs<UiStatusProfile.Loaded>(state)
                assertEquals(mockUser.name, state.uiStateProfileLoaded.name)
            }
        }
    }

    @Test
    fun `when ChangeName command, uiState should update name`() = runTest {
        val newName = "Name"
        
        viewModel.uiState.test {
            skipItems(1) 

            viewModel.processCommand(ProfileCommand.ChangeName(newName))

            val state = awaitItem() as UiStatusProfile.Loaded
            assertEquals(newName, state.uiStateProfileLoaded.name)
        }
    }

    @Test
    fun `when ChangeEmail command, uiState should update email`() = runTest {
        val newEmail = "new_email@mail.ru"
        
        viewModel.uiState.test {
            skipItems(1)

            viewModel.processCommand(ProfileCommand.ChangeEmail(newEmail))

            val state = awaitItem() as UiStatusProfile.Loaded
            assertEquals(newEmail, state.uiStateProfileLoaded.email)
        }
    }

    @Test
    fun `when toggle visibility commands, state should update correctly`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem() as UiStatusProfile.Loaded

            viewModel.processCommand(ProfileCommand.ChangeCurrentPasswordVisibility)
            assertEquals(!initialState.uiStateProfileLoaded.isCurrentPasswordVisible, 
                (awaitItem() as UiStatusProfile.Loaded).uiStateProfileLoaded.isCurrentPasswordVisible)

            viewModel.processCommand(ProfileCommand.ChangeProjectsVisibility)
            assertEquals(!initialState.uiStateProfileLoaded.areProjectsVisible, 
                (awaitItem() as UiStatusProfile.Loaded).uiStateProfileLoaded.areProjectsVisible)
        }
    }

    @Test
    fun `when ChangeProjectName command, state should update project name`() = runTest {
        val newProjectName = "App"
        
        viewModel.uiState.test {
            skipItems(1)

            viewModel.processCommand(ProfileCommand.ChangeProjectName(newProjectName))

            val state = awaitItem() as UiStatusProfile.Loaded
            assertEquals(newProjectName, state.uiStateProfileLoaded.projectName)
        }
    }

    @Test
    fun `when ChangeProgramingLanguage command, state should update language`() = runTest {
        val newLang = ProgramingLanguage(id = 10, name = "Kotlin", description = "", imageName = "")
        
        viewModel.uiState.test {
            skipItems(1)

            viewModel.processCommand(ProfileCommand.ChangeProgramingLanguage(newLang))

            val state = awaitItem() as UiStatusProfile.Loaded
            assertEquals(newLang, state.uiStateProfileLoaded.projectProgramingLanguage)
        }
    }

    @Test
    fun `when passwords change, uiState should be updated`() = runTest {
        val pass = "secret123"
        
        viewModel.uiState.test {
            skipItems(1)

            viewModel.processCommand(ProfileCommand.ChangeCurrentPassword(pass))
            assertEquals(pass, (awaitItem() as UiStatusProfile.Loaded).uiStateProfileLoaded.currentPassword)

            viewModel.processCommand(ProfileCommand.ChangeNewPassword(pass))
            assertEquals(pass, (awaitItem() as UiStatusProfile.Loaded).uiStateProfileLoaded.newPassword)
        }
    }
}