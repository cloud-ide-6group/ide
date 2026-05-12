package ru.vsu.front

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.domain.usecase.DeleteNotificationUseCase
import ru.vsu.front.domain.usecase.ObserveNotificationsUseCase
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.notifications.NotificationsCommand
import ru.vsu.front.notifications.NotificationsEffect
import ru.vsu.front.notifications.NotificationsViewModel
import ru.vsu.front.notifications.UiStateNotifications
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NotificationsViewModelTest {

    private lateinit var deleteNotificationUseCase: DeleteNotificationUseCase
    private lateinit var observeNotificationsUseCase: ObserveNotificationsUseCase
    private lateinit var dispatcherProvider: DispatcherProvider

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        deleteNotificationUseCase = mockk()
        observeNotificationsUseCase = mockk()

        dispatcherProvider = mockk {
            every { main } returns testDispatcher
            every { io } returns testDispatcher
            every { default } returns testDispatcher
        }
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    private fun createMockNotification(id: Int): Notification {
        return mockk {
            every { notificationId } returns id
        }
    }

    @Test
    fun `init successfully observes notifications and sets Loaded state`() = runTest(testDispatcher) {
        val dummyNotifications = listOf(createMockNotification(1), createMockNotification(2))
        every { observeNotificationsUseCase() } returns flowOf(dummyNotifications)

        val viewModel = NotificationsViewModel(
            deleteNotificationUseCase,
            observeNotificationsUseCase,
            dispatcherProvider
        )

        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertIs<UiStateNotifications.Loaded>(state)
        assertEquals(2, state.notifications.size)
        assertEquals(1, state.notifications[0].notificationId)
    }

    @Test
    fun `init sets Error state when flow throws exception`() = runTest(testDispatcher) {
        every { observeNotificationsUseCase() } returns flow { throw RuntimeException("Network Error") }

        val viewModel = NotificationsViewModel(
            deleteNotificationUseCase,
            observeNotificationsUseCase,
            dispatcherProvider
        )

        testScheduler.advanceUntilIdle()

        assertEquals(UiStateNotifications.Error, viewModel.uiState.value)
    }

    @Test
    fun `init sets Error state when observation times out after 20 seconds`() = runTest(testDispatcher) {
        val hangingFlow = MutableSharedFlow<List<Notification>>()
        every { observeNotificationsUseCase() } returns hangingFlow

        val viewModel = NotificationsViewModel(
            deleteNotificationUseCase,
            observeNotificationsUseCase,
            dispatcherProvider
        )

        assertEquals(UiStateNotifications.Loading, viewModel.uiState.value)

        advanceTimeBy(20001)

        assertEquals(UiStateNotifications.Error, viewModel.uiState.value)
    }

    @Test
    fun `processCommand RetryObservingNotifications triggers observation again`() = runTest(testDispatcher) {
        every { observeNotificationsUseCase() } returns flow { throw RuntimeException("Error 1") }

        val viewModel = NotificationsViewModel(
            deleteNotificationUseCase,
            observeNotificationsUseCase,
            dispatcherProvider
        )
        testScheduler.advanceUntilIdle()

        assertEquals(UiStateNotifications.Error, viewModel.uiState.value)

        every { observeNotificationsUseCase() } returns flowOf(listOf(createMockNotification(99)))

        viewModel.processCommand(NotificationsCommand.RetryObservingNotifications)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertIs<UiStateNotifications.Loaded>(state)
        assertEquals(99, state.notifications.first().notificationId)
    }

    @Test
    fun `processCommand DeleteNotification removes item optimistically and succeeds`() = runTest(testDispatcher) {
        val n1 = createMockNotification(1)
        val n2 = createMockNotification(2)
        every { observeNotificationsUseCase() } returns flowOf(listOf(n1, n2))
        coEvery { deleteNotificationUseCase(1) } returns Response.Success(Unit)

        val viewModel = NotificationsViewModel(
            deleteNotificationUseCase,
            observeNotificationsUseCase,
            dispatcherProvider
        )
        testScheduler.advanceUntilIdle()

        var state = viewModel.uiState.value as UiStateNotifications.Loaded
        assertEquals(2, state.notifications.size)

        viewModel.processCommand(NotificationsCommand.DeleteNotification(1))
        testScheduler.advanceUntilIdle()

        state = viewModel.uiState.value as UiStateNotifications.Loaded
        assertEquals(1, state.notifications.size)
        assertEquals(2, state.notifications.first().notificationId)

        coVerify(exactly = 1) { deleteNotificationUseCase(1) }
    }

    @Test
    fun `processCommand DeleteNotification emits ShowMessage on server error`() = runTest(testDispatcher) {
        val n1 = createMockNotification(1)
        every { observeNotificationsUseCase() } returns flowOf(listOf(n1))

        val mockError = mockk<RequestError.Conflict> {
            every { message } returns "Ошибка удаления"
        }

        coEvery { deleteNotificationUseCase(1) } returns Response.Error<Any>(mockError)

        val viewModel = NotificationsViewModel(
            deleteNotificationUseCase,
            observeNotificationsUseCase,
            dispatcherProvider
        )
        testScheduler.advanceUntilIdle()

        viewModel.events.test {
            viewModel.processCommand(NotificationsCommand.DeleteNotification(1))
            testScheduler.advanceUntilIdle()

            assertEquals(NotificationsEffect.ShowMessage("Ошибка удаления"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}