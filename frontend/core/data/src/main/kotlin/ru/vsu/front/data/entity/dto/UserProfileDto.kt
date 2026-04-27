package ru.vsu.front.data.entity.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO профиля пользователя.
 *
 * @property name Имя пользователя.
 * @property email Почта пользователя.
 * @property photo Аватар пользователя в формате Base64.
 * @property projects Проекты, в которых состоит пользователи.
 */
@Serializable
data class UserProfileDto(
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("photo") val photo: String,
    @SerialName("projects") val projects: List<ProjectDto>
)