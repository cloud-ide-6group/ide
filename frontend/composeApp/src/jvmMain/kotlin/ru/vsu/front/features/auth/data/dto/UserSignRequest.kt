@file:Suppress("SpellCheckingInspection")

package ru.vsu.front.features.auth.data.dto

import kotlinx.serialization.Serializable

/**
 * Запрос на регистрацию пользователя
 *
 * @param name Имя пользователя
 * @param email Почта пользователя
 * @param password Пароль пользователя
 */
@Serializable
data class UserSignRequest(
    val name: String,
    val email: String,
    val password: String,
)