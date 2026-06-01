package ru.vsu.front.data.entity.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO пользователя в проекте.
 *
 * @property userId Идентификатор пользователя.
 * @property name Название проекта.
 */
@Serializable
data class UserDto(
    @SerialName("id") val userId: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
)