package ru.vsu.front.model.entity

/**
 * Обертка для результата выполнения запроса.
 */
sealed interface Response<out T> {

    /**
     * Представляет успешное выполнение запроса.
     *
     * @property data Результат, который возвращает сервер.
     */
    data class Success<T>(val data: T) : Response<T>

    /**
     * Представляет ошибку при выполнении запроса.
     *
     * @property requestError Ошибка при запросе.
     */
    data class Error<T>(val requestError: RequestError) : Response<T>
}