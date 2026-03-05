@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.domain

/**
 * Пользователь
 *
 * @param name Имя пользователя
 * @param email Почта пользователя
 */
data class User(
    val name: String,
    val email: String
)