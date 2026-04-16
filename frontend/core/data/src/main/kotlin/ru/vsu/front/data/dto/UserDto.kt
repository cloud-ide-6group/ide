package ru.vsu.front.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO сессии пользователя.
 *
 * @property name Имя пользователя.
 * @property email Почта пользователя.
 * @property photo Аватар пользователя в формате Base64.
 */
@Serializable
data class UserDto(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("photo") val photo: String,
//    @SerialName("projects") val projects: List<ProjectDto>
)