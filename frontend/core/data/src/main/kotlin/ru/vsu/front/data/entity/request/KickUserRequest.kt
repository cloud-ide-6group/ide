package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Запрос исключения пользователя из проекта.
 *
 * @property userEmail Почта пользователя.
 * @property projectId Идентификатор проекта.
 */
@Serializable
data class KickUserRequest(
    @SerialName("invited_user_email") val userEmail: String,
    @SerialName("project_id") val projectId: Int
)
