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
import ru.vsu.front.domain.usecase.GetProfileUseCase
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.UserProfile
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

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setup() {

    }
}