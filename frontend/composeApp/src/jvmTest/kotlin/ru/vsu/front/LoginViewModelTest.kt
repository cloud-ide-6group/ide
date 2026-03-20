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
import ru.vsu.front.features.auth.ui.LoginCommand
import ru.vsu.front.features.auth.ui.LoginEffect
import ru.vsu.front.features.auth.ui.LoginViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LoginViewModelTest {

    private val loginUseCase = mockk<LoginUseCase>()
    private val tokenStorage = mockk<TokenStorage>()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(loginUseCase, tokenStorage, object : DispatcherProvider {
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val default: CoroutineDispatcher = Dispatchers.Default
        })
    }

    @Test
    fun `when email changes, uiState should be updated`() = runTest {
        val newEmail = "new email@mail.meil"
        viewModel.uiStateLogin.test {
            val initialState = awaitItem()

            assertEquals("", initialState.email)

            viewModel.processCommand(LoginCommand.ChangeEmail(newEmail))

            val newState = awaitItem()

            assertEquals(newEmail, newState.email)
        }
    }

    @Test
    fun `when password changes, uiState should be updated`() = runTest {
        val newPassword = "new_password"
        viewModel.uiStateLogin.test {
            val initialState = awaitItem()

            assertEquals("", initialState.password)

            viewModel.processCommand(LoginCommand.ChangePassword(newPassword))

            val newState = awaitItem()

            assertEquals(newPassword, newState.password)
        }
    }

    @Test
    fun `when password visibility clicked, uiState should toggle visibility`() = runTest {
        viewModel.uiStateLogin.test {
            val initialState = awaitItem()

            assertFalse(initialState.isPasswordVisible)

            viewModel.processCommand(LoginCommand.ChangePasswordVisibility)

            val updatedState1 = awaitItem()

            assertTrue(updatedState1.isPasswordVisible)

            viewModel.processCommand(LoginCommand.ChangePasswordVisibility)

            val updatedState2 = awaitItem()

            assertFalse(updatedState2.isPasswordVisible)
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

        viewModel.processCommand(LoginCommand.ChangeEmail(emailTest))
        viewModel.processCommand(LoginCommand.ChangePassword(passwordTest))
        viewModel.processCommand(LoginCommand.ClickLogin)

        coVerify(exactly = 1) {
            tokenStorage.saveToken(accessTokenTest, true)
        }

        coVerify(exactly = 1) {
            tokenStorage.saveToken(refreshTokenTest, false)
        }
    }

    @Test
    fun `when login fails, ShowError effect is sent`() = runTest {
        val errorMessage = "error"
        val authError = AuthError.BadRequest(message = errorMessage)

        coEvery { loginUseCase(any(), any()) } returns AuthResult.Error(authError)

        viewModel.events.test {
            viewModel.processCommand(LoginCommand.ClickLogin)

            val effect = awaitItem()
            assertTrue(effect is LoginEffect.ShowError)
            assertEquals(errorMessage, effect.message)
        }
    }
}