package ru.vsu.front.model.entity

data class Notification(
    val projectId: Int,
    val projectName: String,
    val ownerName: String,
    val ownerPhoto: String,
)
