package ru.vsu.front.model.entity

/**
 * DTO пользователя в проекте.
 *
 * @property userId Идентификатор пользователя.
 * @property name Название проекта.
 */
data class User(
    val userId: Int,
    val name: String
)