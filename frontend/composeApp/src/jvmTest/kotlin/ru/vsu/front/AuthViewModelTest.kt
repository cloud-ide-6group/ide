package ru.vsu.front

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import ru.vsu.front.common.di.dispatcher_provider.DispatcherProvider
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.features.auth.domain.entity.AuthError
import ru.vsu.front.features.auth.domain.entity.AuthResult
import ru.vsu.front.features.auth.domain.entity.AuthTokens
import ru.vsu.front.features.auth.domain.entity.UserSession
import ru.vsu.front.features.auth.domain.usecase.LoginUseCase
import ru.vsu.front.features.auth.domain.usecase.SignUseCase
import ru.vsu.front.features.auth.ui.AuthCommand
import ru.vsu.front.features.auth.ui.AuthEffect
import ru.vsu.front.features.auth.ui.AuthViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Тесты для [AuthViewModel].
 * * Тестируют логику авторизации ([LoginUseCase]) и регистрации ([SignUseCase]).
 * * Проверяют сохранение JWT-токенов в [TokenStorage].
 * * Проверяют отправку сайд-эффектов.
 */
class AuthViewModelTest {

    private val loginUseCase = mockk<LoginUseCase>()
    private val signUseCase = mockk<SignUseCase>()
    private val tokenStorage = mockk<TokenStorage>()

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setup() {
        viewModel = AuthViewModel(
            loginUseCase = loginUseCase,
            signUseCase = signUseCase,
            tokenStorage = tokenStorage,
            dispatcherProvider = object : DispatcherProvider {
                override val main: CoroutineDispatcher = Dispatchers.Main
                override val io: CoroutineDispatcher = Dispatchers.IO
                override val default: CoroutineDispatcher = Dispatchers.Default
            }
        )
    }

    @Test
    fun `when name changes, uiState should be updated`() = runTest {
        val newName = "newName"
        viewModel.uiState.test {
            assertEquals("", awaitItem().name)

            viewModel.processCommand(AuthCommand.ChangeName(newName))

            assertEquals(newName, awaitItem().name)
        }
    }

    @Test
    fun `when email changes, uiState should be updated`() = runTest {
        val newEmail = "new_email@mail.com"

        viewModel.uiState.test {
            assertEquals("", awaitItem().email)

            viewModel.processCommand(AuthCommand.ChangeEmail(newEmail))

            assertEquals(newEmail, awaitItem().email)
        }
    }

    @Test
    fun `when password changes, uiState should be updated`() = runTest {
        val newPassword = "newPassword"

        viewModel.uiState.test {
            assertEquals("", awaitItem().password)

            viewModel.processCommand(AuthCommand.ChangePassword(newPassword))

            assertEquals(newPassword, awaitItem().password)
        }
    }

    @Test
    fun `when confirmed password changes, uiState should be updated`() = runTest {
        val newConfirmedPassword = "newConfirmedPassword"

        viewModel.uiState.test {
            assertEquals("", awaitItem().confirmedPassword)

            viewModel.processCommand(AuthCommand.ChangeConfirmedPassword(newConfirmedPassword))

            assertEquals(newConfirmedPassword, awaitItem().confirmedPassword)
        }
    }

    @Test
    fun `when password visibility clicked, uiState should toggle visibility`() = runTest {
        viewModel.uiState.test {

            assertFalse(awaitItem().isPasswordVisible)

            viewModel.processCommand(AuthCommand.ChangePasswordVisibility)
            assertTrue(awaitItem().isPasswordVisible)

            viewModel.processCommand(AuthCommand.ChangePasswordVisibility)
            assertFalse(awaitItem().isPasswordVisible)
        }
    }

    @Test
    fun `when confirmed password visibility clicked, uiState should toggle visibility`() = runTest {
        viewModel.uiState.test {
            assertFalse(awaitItem().isConfirmedPasswordVisible)

            viewModel.processCommand(AuthCommand.ChangeConfirmedPasswordVisibility)
            assertTrue(awaitItem().isConfirmedPasswordVisible)

            viewModel.processCommand(AuthCommand.ChangeConfirmedPasswordVisibility)
            assertFalse(awaitItem().isConfirmedPasswordVisible)
        }
    }

    @Test
    fun `when login success, tokens should be saved`() = runTest {
        val emailTest = "email@email.com"
        val passwordTest = "password@password"
        val accessTokenTest = "access-token"
        val refreshTokenTest = "refresh-token"

        val mockTokens = mockk<AuthTokens> {
            every { accessToken } returns accessTokenTest
            every { refreshToken } returns refreshTokenTest
        }
        val mockUserSession = mockk<UserSession> {
            every { tokens } returns mockTokens
        }

        coEvery { loginUseCase(emailTest, passwordTest) } returns AuthResult.Success(mockUserSession)
        coEvery { tokenStorage.saveToken(any(), any()) } returns Unit

        viewModel.processCommand(AuthCommand.ChangeEmail(emailTest))
        viewModel.processCommand(AuthCommand.ChangePassword(passwordTest))
        viewModel.processCommand(AuthCommand.ClickLogin)

        coVerify(exactly = 1) { tokenStorage.saveToken(accessTokenTest, true) }
        coVerify(exactly = 1) { tokenStorage.saveToken(refreshTokenTest, false) }
    }

    @Test
    fun `when login fails, ShowError effect is sent`() = runTest {
        val emailTest = "email@email.com"
        val errorMessage = "error"
        val authError = AuthError.BadRequest(message = errorMessage)

        coEvery { loginUseCase(any(), any()) } returns AuthResult.Error(authError)

        viewModel.processCommand(AuthCommand.ChangeEmail(emailTest))

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickLogin)

            val effect = awaitItem()
            assertTrue(effect is AuthEffect.ShowError)
            assertEquals(errorMessage, effect.message)
        }
    }

    @Test
    fun `when login clicks, button is disabled and then enabled`() = runTest {
        val emailTest = "email@email.com"
        val passwordTest = "password@password"

        coEvery { loginUseCase(emailTest, passwordTest) } returns AuthResult.Success(mockk(relaxed = true))
        coEvery { tokenStorage.saveToken(any(), any()) } returns Unit

        viewModel.uiState.test {
            assertTrue(awaitItem().buttonEnabled)

            viewModel.processCommand(AuthCommand.ChangeEmail(emailTest))
            viewModel.processCommand(AuthCommand.ChangePassword(passwordTest))
            viewModel.processCommand(AuthCommand.ClickLogin)

            skipItems(2)

            assertFalse(awaitItem().buttonEnabled)
            assertTrue(awaitItem().buttonEnabled)
        }
    }

    @Test
    fun `when sign up success, tokens should be saved`() = runTest {
        val nameTest = "name_Test"
        val emailTest = "email@email.com"
        val passwordTest = "password@password"
        val confirmedPasswordTest = "password@password"
        val accessTokenTest = "access-token"
        val refreshTokenTest = "refresh-token"

        val mockTokens = mockk<AuthTokens> {
            every { accessToken } returns accessTokenTest
            every { refreshToken } returns refreshTokenTest
        }
        val mockUserSession = mockk<UserSession> {
            every { tokens } returns mockTokens
        }

        coEvery { signUseCase(nameTest, emailTest, passwordTest) } returns AuthResult.Success(mockUserSession)
        coEvery { tokenStorage.saveToken(any(), any()) } returns Unit

        viewModel.processCommand(AuthCommand.ChangeName(nameTest))
        viewModel.processCommand(AuthCommand.ChangeEmail(emailTest))
        viewModel.processCommand(AuthCommand.ChangePassword(passwordTest))
        viewModel.processCommand(AuthCommand.ChangeConfirmedPassword(confirmedPasswordTest))
        viewModel.processCommand(AuthCommand.ClickSignUp)

        coVerify(exactly = 1) { tokenStorage.saveToken(accessTokenTest, true) }
        coVerify(exactly = 1) { tokenStorage.saveToken(refreshTokenTest, false) }
    }

    @Test
    fun `when sign up fails, ShowError effect is sent`() = runTest {
        val emailTest = "email@email.com"
        val errorMessage = "error"
        val authError = AuthError.BadRequest(message = errorMessage)

        coEvery { signUseCase(any(), any(), any()) } returns AuthResult.Error(authError)

        viewModel.processCommand(AuthCommand.ChangeEmail(emailTest))

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickSignUp)

            val effect = awaitItem()
            assertTrue(effect is AuthEffect.ShowError)
            assertEquals(errorMessage, effect.message)
        }
    }

    @Test
    fun `when sign up clicks and passwords do not match, ShowError effect should be sent`() = runTest {
        val nameTest = "name_Test"
        val emailTest = "email@email.com"
        val passwordTest = "12345"
        val confirmedPasswordTest = "54321"

        viewModel.processCommand(AuthCommand.ChangeName(nameTest))
        viewModel.processCommand(AuthCommand.ChangeEmail(emailTest))
        viewModel.processCommand(AuthCommand.ChangePassword(passwordTest))
        viewModel.processCommand(AuthCommand.ChangeConfirmedPassword(confirmedPasswordTest))

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickSignUp)

            val event = awaitItem()
            assertTrue(event is AuthEffect.ShowError)
            assertEquals("Пароли не совпадают", event.message)
        }
    }

    @Test
    fun `when invalid email, ShowError effect is sent on login and signup`() = runTest {
        val invalidEmail = "bad_email"

        viewModel.processCommand(AuthCommand.ChangeEmail(invalidEmail))

        viewModel.events.test {
            viewModel.processCommand(AuthCommand.ClickLogin)

            val loginEvent = awaitItem()
            assertTrue(loginEvent is AuthEffect.ShowError)
            assertEquals("Недопустимая почта", loginEvent.message)

            viewModel.processCommand(AuthCommand.ClickSignUp)

            val signUpEvent = awaitItem()
            assertTrue(signUpEvent is AuthEffect.ShowError)
            assertEquals("Недопустимая почта", signUpEvent.message)
        }
    }
}