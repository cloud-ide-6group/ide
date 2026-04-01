package ru.vsu.front.model.entity

/**
 * Текущая сессия авторизованного пользователя.
 *
 * На текущий коммит класс актуален, однако в будущем будет изменён.
 *
 * @property user Пользователь.
 * @property tokens JWT токены.
 * @property projects Список проектов пользователя.
 */
data class UserData(
    val user: User,
    val tokens: AuthTokens,
    val projects: List<Project> = emptyList(),
)