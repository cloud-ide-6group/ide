package ru.vsu.front.profile

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.*
import ru.vsu.front.domain.validation.EmailMatcher
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserProfile
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private lateinit var getProfileUseCase: GetProfileUseCase
    private lateinit var getProgramingLanguagesUseCase: GetProgramingLanguagesUseCase
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private lateinit var updateProfileDataUseCase: UpdateProfileDataUseCase
    private lateinit var updateProfilePasswordUseCase: UpdateProfilePasswordUseCase
    private lateinit var updateProfilePhotoUseCase: UpdateProfilePhotoUseCase
    private lateinit var observeNotificationsUseCase: ObserveNotificationsUseCase
    private lateinit var dispatcherProvider: DispatcherProvider
    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyProfile = UserProfile(
        id = 1,
        name = "Ivan",
        email = "ivan@test.ru",
        photo = "photo",
        projects = emptyList()
    )
    private val dummyLanguages = listOf(
        ProgramingLanguage(id = 1, name = "Kotlin", description = ""),
        ProgramingLanguage(id = 2, name = "Java", description = "")
    )

    @BeforeTest
    fun setUp() {
        getProfileUseCase = mockk()
        getProgramingLanguagesUseCase = mockk()
        createProjectUseCase = mockk()
        updateProfileDataUseCase = mockk()
        updateProfilePasswordUseCase = mockk()
        updateProfilePhotoUseCase = mockk()
        observeNotificationsUseCase = mockk()

        dispatcherProvider = mockk {
            every { main } returns testDispatcher
            every { io } returns testDispatcher
            every { default } returns testDispatcher
        }

        every { observeNotificationsUseCase() } returns emptyFlow()

        mockkObject(EmailMatcher)
        every { EmailMatcher.isValid(any()) } returns true
    }

    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    /**
     * Фабричный метод для создания ViewModel.
     * Позволяет настроить моки (coEvery) в самом тесте ДО вызова init {}.
     */
    private fun createViewModel(): ProfileViewModel {
        return ProfileViewModel(
            getProfileUseCase = getProfileUseCase,
            getProgramingLanguagesUseCase = getProgramingLanguagesUseCase,
            createProjectUseCase = createProjectUseCase,
            updateProfileDataUseCase = updateProfileDataUseCase,
            updateProfilePasswordUseCase = updateProfilePasswordUseCase,
            updateProfilePhotoUseCase = updateProfilePhotoUseCase,
            observeNotificationsUseCase = observeNotificationsUseCase,
            dispatcherProvider = dispatcherProvider
        )
    }

    /**
     * Настраивает успешный ответ для первичной загрузки данных.
     */
    private fun setupSuccessInitialization() {
        coEvery { getProfileUseCase() } returns Response.Success(dummyProfile)
        coEvery { getProgramingLanguagesUseCase() } returns Response.Success(dummyLanguages)
    }

    @Test
    fun `init loads profile and languages successfully and sets Loaded state`() = runTest {
        setupSuccessInitialization()

        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertIs<UiStatusProfile.Loaded>(state)

        val loadedState = state.uiStateProfileLoaded
        assertEquals("Ivan", loadedState.name)
        assertEquals("Kotlin", loadedState.selectedProgramingLanguageForProject?.name)
        assertEquals(2, loadedState.programingLanguages.size)
    }

    @Test
    fun `init sets Error state when profile loading fails`() = runTest {
        coEvery { getProfileUseCase() } returns Response.Error(mockk(relaxed = true))
        coEvery { getProgramingLanguagesUseCase() } returns Response.Success(dummyLanguages)

        val viewModel = createViewModel()

        assertEquals(UiStatusProfile.Error, viewModel.uiState.value)
    }

    @Test
    fun `processCommand ChangeName updates state and sets hasChangesName to true`() = runTest {
        setupSuccessInitialization()
        val viewModel = createViewModel()

        viewModel.processCommand(ProfileCommand.ChangeName("Alex"))

        val state = viewModel.uiState.value as UiStatusProfile.Loaded
        assertEquals("Alex", state.uiStateProfileLoaded.name)
        assertTrue(state.uiStateProfileLoaded.hasChangesName)
    }

    @Test
    fun `processCommand UpdateData calls useCase and emits success message`() = runTest {
        setupSuccessInitialization()
        val viewModel = createViewModel()

        viewModel.processCommand(ProfileCommand.ChangeName("Alex"))

        coEvery { updateProfileDataUseCase(email = "ivan@test.ru", name = "Alex") } returns Response.Success(Unit)

        viewModel.events.test {
            viewModel.processCommand(ProfileCommand.UpdateData)

            assertEquals(ProfileEffect.ShowMessage("Данные обновлены"), awaitItem())

            val state = viewModel.uiState.value as UiStatusProfile.Loaded
            assertEquals("Alex", state.uiStateProfileLoaded.initialName)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `processCommand CreateProject creates project and updates list`() = runTest {
        setupSuccessInitialization()
        val viewModel = createViewModel()

        viewModel.processCommand(ProfileCommand.ChangeProgramingLanguage(dummyLanguages[1]))
        viewModel.processCommand(ProfileCommand.ChangeProjectName("My Project"))

        coEvery { createProjectUseCase(programingLanguageId = 2, projectName = "My Project") } returns Response.Success(
            99
        )

        viewModel.events.test {
            viewModel.processCommand(ProfileCommand.CreateProject)

            assertEquals(ProfileEffect.ShowMessage("Проект создан"), awaitItem())

            val state = viewModel.uiState.value as UiStatusProfile.Loaded
            val addedProject = state.uiStateProfileLoaded.projects.first()
            assertEquals(99, addedProject.id)
            assertEquals("My Project", addedProject.name)

            assertEquals("", state.uiStateProfileLoaded.projectName)

            cancelAndIgnoreRemainingEvents()
        }
    }
}