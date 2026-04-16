package ru.vsu.front.model.entity

/**
 * Базовый класс для представления ошибок при сетевых запросах.
 *
 * @property message Сообщение об ошибке.
 */
sealed class RequestError(open val message: String) {
    /**
     * 400: Неверный запрос.
     * */
    data class BadRequest(override val message: String) : RequestError(message)

    /**
     * 401: Неавторизированный доступ.
     * */
    data class Unauthorized(override val message: String) : RequestError(message)

    /**
     * 403: Доступ запрещен.
     * */
    data class Forbidden(override val message: String) : RequestError(message)

    /**
     * 404: Пользователь не найден.
     * */
    data class NotFound(override val message: String) : RequestError(message)

    /**
     * Ошибка сети (отсутствует интернет или сервер недоступен).
     * */
    data class NetworkException(override val message: String = "Ошибка сети") : RequestError(message)

    /** 
     * Неизвестная ошибка.
     * */
    data class UnknownError(override val message: String = "Неизвестная ошибка") : RequestError(message)
}