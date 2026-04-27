package ru.vsu.front.authorization

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.LoginUseCase
import ru.vsu.front.domain.usecase.SignUseCase
import ru.vsu.front.domain.validation.EmailMatcher
import ru.vsu.front.model.entity.AuthTokens
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var signUseCase: SignUseCase
    private lateinit var tokenStorage: TokenStorage
    private lateinit var authManager: AuthManager
    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var viewModel: AuthViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        loginUseCase = mockk()
        signUseCase = mockk()
        tokenStorage = mockk(relaxed = true)
        authManager = mockk(relaxed = true)

        dispatcherProvider = mockk {
            every { main } returns testDispatcher
            every { io } returns testDispatcher
            every { default } returns testDispatcher
        }

        mockkObject(EmailMatcher)

        viewModel = AuthViewModel(
            loginUseCase = loginUseCase,
            signUseCase = signUseCase,
            tokenStorage = tokenStorage,
            dispatcherProvider = dispatcherProvider,
            authManager = authManager
        )
    }

    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `processCommand ChangeEmail updates email in state`() {
        viewModel.processCommand(AuthCommand.ChangeEmail("test@mail.ru"))
        assertEquals("test@mail.ru", viewModel.uiState.value.email)
    }

    @Test
    fun `processCommand ChangePasswordVisibility toggles state`() {
        val initialState = viewModel.uiState.value.isPasswordVisible
        viewModel.processCommand(AuthCommand.ChangePasswordVisibility)
        assertEquals(!initialState, viewModel.uiState.value.isPasswordVisible)
    }

    @Test
    fun `ClickLogin emits ShowError event when email is invalid`() = runTest {
        every { EmailMatcher.isValid("invalid_email") } returns false
        viewModel.processCommand(AuthCommand.ChangeEmail("invalid_email"))

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickLogin)

            assertEquals(AuthEffect.ShowError("Недопустимая почта"), awaitItem())

            coVerify(exactly = 0) { loginUseCase(any(), any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ClickLogin calls useCase and emits Error on failure`() = runTest {
        val testEmail = "test@mail.ru"
        val testPassword = "123"
        every { EmailMatcher.isValid(testEmail) } returns true
        viewModel.processCommand(AuthCommand.ChangeEmail(testEmail))
        viewModel.processCommand(AuthCommand.ChangePassword(testPassword))

        val mockError = mockk<RequestError> { every { message } returns "Ошибка сервера" }
        coEvery { loginUseCase(testEmail, testPassword) } returns Response.Error(mockError)

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickLogin)

            assertEquals(AuthEffect.ShowError("Ошибка сервера"), awaitItem())
            assertTrue(viewModel.uiState.value.buttonEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ClickLogin success saves tokens and calls AuthManager`() = runTest {
        val testEmail = "test@mail.ru"
        val testPassword = "123"
        every { EmailMatcher.isValid(testEmail) } returns true
        viewModel.processCommand(AuthCommand.ChangeEmail(testEmail))
        viewModel.processCommand(AuthCommand.ChangePassword(testPassword))

        val mockTokens = AuthTokens(accessToken = "access", refreshToken = "refresh")
        coEvery { loginUseCase(testEmail, testPassword) } returns Response.Success(mockTokens)
        every { tokenStorage.getUserIdFromToken() } returns 99

        viewModel.processCommand(AuthCommand.ClickLogin)

        coVerify { tokenStorage.saveToken("access", true) }
        coVerify { tokenStorage.saveToken("refresh", false) }

        coVerify(exactly = 1) { authManager.onLoginSuccess(99) }

        assertFalse(viewModel.uiState.value.buttonEnabled)
    }

    @Test
    fun `ClickSignUp emits ShowError when passwords do not match`() = runTest {
        val testEmail = "test@mail.ru"
        every { EmailMatcher.isValid(testEmail) } returns true

        viewModel.processCommand(AuthCommand.ChangeEmail(testEmail))
        viewModel.processCommand(AuthCommand.ChangePassword("123"))
        viewModel.processCommand(AuthCommand.ChangeConfirmedPassword("321"))

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickSignUp)

            assertEquals(AuthEffect.ShowError("Пароли не совпадают"), awaitItem())
            coVerify(exactly = 0) { signUseCase(any(), any(), any()) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ClickSignUp success calls signUseCase and saves tokens`() = runTest {
        val testName = "Ivan"
        val testEmail = "test@mail.ru"
        val testPassword = "123"

        every { EmailMatcher.isValid(testEmail) } returns true
        viewModel.processCommand(AuthCommand.ChangeName(testName))
        viewModel.processCommand(AuthCommand.ChangeEmail(testEmail))
        viewModel.processCommand(AuthCommand.ChangePassword(testPassword))
        viewModel.processCommand(AuthCommand.ChangeConfirmedPassword(testPassword))

        val mockTokens = AuthTokens(accessToken = "access_new", refreshToken = "refresh_new")
        coEvery { signUseCase(testName, testEmail, testPassword) } returns Response.Success(mockTokens)
        every { tokenStorage.getUserIdFromToken() } returns 42

        viewModel.processCommand(AuthCommand.ClickSignUp)

        coVerify { signUseCase(testName, testEmail, testPassword) }
        coVerify { authManager.onLoginSuccess(42) }
    }
}