@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ошибка
 *
 * @param message Сообщение об ошибке
 */
@Serializable
data class ErrorResponseDto(
    /**
     * TODO Поменять имя параметра error на message, когда на беке будет обновлено
     */
    @SerialName("error") val message: String
)
