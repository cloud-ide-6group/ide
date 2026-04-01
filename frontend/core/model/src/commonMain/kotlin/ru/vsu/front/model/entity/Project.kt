package ru.vsu.front.model.entity

/**
 *
 *
 *
 */
data class Project(
    val id: Int,
    val name: String,
    val ownerId: Int,
    val programmingLanguageId: Int
)
