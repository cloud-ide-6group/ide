package ru.vsu.front.features.auth.domain.entity

/**
 * Пользователь
 *
 * @param name Имя пользователя
 * @param email Почта пользователя
 * @param photoPath Путь до аватара пользователя [На сервере]
 */
data class User(
    val name: String,
    val email: String,
    val photoPath: String,
)