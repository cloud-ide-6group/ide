package ru.vsu.front.model.entity

/**
 * Модель данных пользователя.
 *
 * @property id Идентификатор пользователя.
 * @property name Имя пользователя.
 * @property email Почта пользователя.
 * @property photo Фото пользователя в формате Base64.
 * @property projects Проекты, в которых состоит пользователь.
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val photo: String,
    val projects: List<Project>
)