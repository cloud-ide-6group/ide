package ru.vsu.front.features.auth.domain.entity

/**
 * Модель данных пользователя.
 *
 * @property name Имя пользователя.
 * @property email Почта пользователя.
 * @property photoPath Путь к аватару пользователя, хранящийся на сервере.
 */
data class User(
    val name: String,
    val email: String,
    val photoPath: String,
)