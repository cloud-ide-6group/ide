package ru.vsu.front

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.auth.AuthState
import ru.vsu.front.datastore.TokenStorage
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthManagerTest {

    @Test
    fun `initial state is Authorized when token storage has valid user id`() = runTest {
        val mockTokenStorage = mockk<TokenStorage> {
            every { getUserIdFromToken() } returns 123
        }
        val authManager = AuthManager(mockTokenStorage)

        authManager.isAuthorized.test {
            assertEquals(AuthState.Authorized(123), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state is NotAuthorized when token storage returns null`() = runTest {
        val mockTokenStorage = mockk<TokenStorage> {
            every { getUserIdFromToken() } returns null
        }
        val authManager = AuthManager(mockTokenStorage)

        authManager.isAuthorized.test {
            assertEquals(AuthState.NotAuthorized, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onLoginSuccess emits Authorized state with correct user id`() = runTest {
        val mockTokenStorage = mockk<TokenStorage> {
            every { getUserIdFromToken() } returns null
        }
        val authManager = AuthManager(mockTokenStorage)

        authManager.isAuthorized.test {
            assertEquals(AuthState.NotAuthorized, awaitItem())

            authManager.onLoginSuccess(456)

            assertEquals(AuthState.Authorized(456), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `logout calls clearTokens and emits NotAuthorized state`() = runTest {
        val mockTokenStorage = mockk<TokenStorage>(relaxed = true) {
            every { getUserIdFromToken() } returns 789
        }
        val authManager = AuthManager(mockTokenStorage)

        authManager.isAuthorized.test {
            assertEquals(AuthState.Authorized(789), awaitItem())

            authManager.logout()

            assertEquals(AuthState.NotAuthorized, awaitItem())

            verify(exactly = 1) { mockTokenStorage.clearTokens() }

            cancelAndIgnoreRemainingEvents()
        }
    }
}