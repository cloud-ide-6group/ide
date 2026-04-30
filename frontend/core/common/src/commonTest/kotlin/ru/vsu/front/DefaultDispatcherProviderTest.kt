package ru.vsu.front

import kotlinx.coroutines.Dispatchers
import ru.vsu.front.common.dispatcher_provider.DefaultDispatcherProvider
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDispatcherProviderTest {

    private val provider = DefaultDispatcherProvider()

    @Test
    fun `main property should return Dispatchers Main`() {
        assertEquals(
            expected = Dispatchers.Main,
            actual = provider.main
        )
    }

    @Test
    fun `io property should return Dispatchers IO`() {
        assertEquals(
            expected = Dispatchers.IO,
            actual = provider.io
        )
    }

    @Test
    fun `default property should return Dispatchers Default`() {
        assertEquals(
            expected = Dispatchers.Default,
            actual = provider.default
        )
    }
}