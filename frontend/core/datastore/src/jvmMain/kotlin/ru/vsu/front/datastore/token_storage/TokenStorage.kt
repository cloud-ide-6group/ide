package ru.vsu.front.datastore.token_storage

import ru.vsu.front.model.entity.AuthTokens

/**
 * Интерфейс хранилища JWT-токенов.
 */
interface TokenStorage {
    /**
     * Возвращает текущие токены.
     * * Возвращает пару [AuthTokens] в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    fun getTokensSync(): AuthTokens?
    /**
     * Возвращает текущие токены.
     * * Асинхронно возвращает [AuthTokens] в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    suspend fun getTokensAsync(): AuthTokens?
    /**
     * Зашифровывает и сохраняет переданный токен в локальное хранилище.
     *
     * @param token Строка-токен в не зашифрованном виде.
     * @param isAccess Тип токена: `true` - Access-токен, `false` - Refresh-токен.
     */
    fun saveToken(token: String, isAccess: Boolean)
    /**
     * Удаляет Access и Refresh токены из хранилища.
     */
    fun clearTokens()
    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @return Идентификатор пользователя или null.
     */
    fun getUserIdFromToken(): Int?
}