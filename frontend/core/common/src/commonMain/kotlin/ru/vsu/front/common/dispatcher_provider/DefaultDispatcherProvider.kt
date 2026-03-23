package ru.vsu.front.common.dispatcher_provider

import kotlinx.coroutines.Dispatchers

/**
 * Конкретный провайдер корутинных диспетчеров.
 */
class DefaultDispatcherProvider : DispatcherProvider {

    /**
     * Диспетчер для работы с UI [Dispatchers.Main].
     */
    override val main = Dispatchers.Main

    /**
     * Диспетчер для операций ввода-вывода [Dispatchers.IO].
     */
    override val io = Dispatchers.IO

    /**
     * Диспетчер для тяжелых вычислительных задач [Dispatchers.Default].
     */
    override val default = Dispatchers.Default
}