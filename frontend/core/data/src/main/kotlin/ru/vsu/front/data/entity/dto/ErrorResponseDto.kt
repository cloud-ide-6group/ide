package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO стандартного ответа сервера с ошибкой.
 *
 * @property message Сообщение об ошибке.
 */
@Serializable
data class ErrorResponseDto(
    @SerialName("message") val message: String
)
