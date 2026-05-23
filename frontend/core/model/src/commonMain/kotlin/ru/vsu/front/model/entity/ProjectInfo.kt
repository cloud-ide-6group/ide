package ru.vsu.front.model.entity

/**
 * DTO информации о проекте.
 *
 * @property languageName Язык программирования в проекте.
 * @property projectId Идентификатор проекта.
 * @property projectName Название проекта.
 * @property isUserOwner Является ли пользователь владельцем
 * @property users Список участников проекта
 */
data class ProjectInfo(
    val languageName: String,
    val projectId: Int,
    val projectName: String,
    val isUserOwner: Boolean,
    val users: List<User>
)
