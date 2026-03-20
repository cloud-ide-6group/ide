package ru.vsu.front.common.di.dispatcher_provider

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Провайдер корутинных диспетчеров.
 * Абстрагирует получение диспетчеров [CoroutineDispatcher], что позволяет
 * легко подменять реальные многопоточные диспетчеры на тестовые при написании Unit-тестов.
 */
interface DispatcherProvider {

    /**
     * Диспетчер для работы с UI.
     * Используется для обновления интерфейса.
     */
    val main: CoroutineDispatcher

    /**
     * Диспетчер для операций ввода-вывода.
     * Оптимизирован для блокирующих задач: запросы в сеть, чтение/запись файлов, работа с БД.
     */
    val io: CoroutineDispatcher

    /**
     * Диспетчер для тяжелых вычислительных задач.
     * Используется для сложной математики, сортировки больших списков.
     */
    val default: CoroutineDispatcher
}