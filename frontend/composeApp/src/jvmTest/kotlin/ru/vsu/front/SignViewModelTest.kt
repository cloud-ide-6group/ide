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
import ru.vsu.front.features.auth.domain.usecase.SignUseCase
import ru.vsu.front.features.auth.ui.SignCommand
import ru.vsu.front.features.auth.ui.SignEffect
import ru.vsu.front.features.auth.ui.SignViewModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SignViewModelTest {
    private val signUseCase = mockk<SignUseCase>()
    private val tokenStorage = mockk<TokenStorage>()

    private lateinit var viewModel: SignViewModel

    @Before
    fun setup() {
        viewModel = SignViewModel(signUseCase, tokenStorage, object : DispatcherProvider {
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val default: CoroutineDispatcher = Dispatchers.Default
        })
    }

    @Test
    fun `when email changes, uiState should be updated`() = runTest {
        val newEmail = "new email@mail.meil"
        viewModel.uiStateSign.test {
            val initialState = awaitItem()

            assertEquals("", initialState.email)

            viewModel.processCommand(SignCommand.ChangeEmail(newEmail))

            val newState = awaitItem()

            assertEquals(newEmail, newState.email)
        }
    }

    @Test
    fun `when name changes, uiState should be updated`() = runTest {
        val newName = "newName"
        viewModel.uiStateSign.test {
            val initialState = awaitItem()

            assertEquals("", initialState.name)

            viewModel.processCommand(SignCommand.ChangeName(newName))

            val newState = awaitItem()

            assertEquals(newName, newState.name)
        }
    }

    @Test
    fun `when password changes, uiState should be updated`() = runTest {
        val newPassword = "newPassword"
        viewModel.uiStateSign.test {
            val initialState = awaitItem()

            assertEquals("", initialState.password)

            viewModel.processCommand(SignCommand.ChangePassword(newPassword))

            val newState = awaitItem()

            assertEquals(newPassword, newState.password)
        }
    }

    @Test
    fun `when confirmed password changes, uiState should be updated`() = runTest {
        val newConfirmedPassword = "newConfirmedPassword"
        viewModel.uiStateSign.test {
            val initialState = awaitItem()

            assertEquals("", initialState.confirmedPassword)

            viewModel.processCommand(SignCommand.ChangeConfirmedPassword(newConfirmedPassword))

            val newState = awaitItem()

            assertEquals(newConfirmedPassword, newState.confirmedPassword)
        }
    }

    @Test
    fun `when password visibility clicked, uiState should toggle visibility`() = runTest {
        viewModel.uiStateSign.test {
            val initialState = awaitItem()

            assertFalse(initialState.isPasswordVisible)

            viewModel.processCommand(SignCommand.ChangePasswordVisibility)

            val updatedState1 = awaitItem()

            assertTrue(updatedState1.isPasswordVisible)

            viewModel.processCommand(SignCommand.ChangePasswordVisibility)

            val updatedState2 = awaitItem()

            assertFalse(updatedState2.isPasswordVisible)
        }
    }

    @Test
    fun `when confirmed password visibility clicked, uiState should toggle visibility`() = runTest {
        viewModel.uiStateSign.test {
            val initialState = awaitItem()

            assertFalse(initialState.isConfirmedPasswordVisible)

            viewModel.processCommand(SignCommand.ChangeConfirmedPasswordVisibility)

            val updatedState1 = awaitItem()

            assertTrue(updatedState1.isConfirmedPasswordVisible)

            viewModel.processCommand(SignCommand.ChangeConfirmedPasswordVisibility)

            val updatedState2 = awaitItem()

            assertFalse(updatedState2.isPasswordVisible)
        }
    }


    @Test
    fun `when sign success, tokens should be saved`() = runTest {
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

        viewModel.processCommand(SignCommand.ChangeName(nameTest))
        viewModel.processCommand(SignCommand.ChangeEmail(emailTest))
        viewModel.processCommand(SignCommand.ChangePassword(passwordTest))
        viewModel.processCommand(SignCommand.ChangeConfirmedPassword(confirmedPasswordTest))
        viewModel.processCommand(SignCommand.ClickSignUp)

        coVerify(exactly = 1) {
            tokenStorage.saveToken(accessTokenTest, true)
        }

        coVerify(exactly = 1) {
            tokenStorage.saveToken(refreshTokenTest, false)
        }
    }

    @Test
    fun `when sign fails, ShowError effect is sent`() = runTest {
        val errorMessage = "error"
        val authError = AuthError.BadRequest(message = errorMessage)

        coEvery { signUseCase(any(), any(), any()) } returns AuthResult.Error(authError)

        viewModel.events.test {
            viewModel.processCommand(SignCommand.ClickSignUp)

            val effect = awaitItem()
            assertTrue(effect is SignEffect.ShowError)
            assertEquals(errorMessage, effect.message)
        }
    }
}