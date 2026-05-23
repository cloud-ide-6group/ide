package ru.vsu.front.data.entity.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InviteUserRequest(
    @SerialName("invited_user_email") val userEmail: String,
    @SerialName("project_name") val projectName: String
)
