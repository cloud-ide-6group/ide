package ru.vsu.front.model.entity

/**
 * Пользователь.
 *
 * @property userId Идентификатор пользователя.
 * @property name Название проекта.
 */
data class User(
    val userId: Int,
    val name: String,
    val email: String
)