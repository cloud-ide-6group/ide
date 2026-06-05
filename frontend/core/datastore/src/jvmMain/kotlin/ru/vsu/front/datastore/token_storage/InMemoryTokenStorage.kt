package ru.vsu.front.datastore.token_storage

import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.datastore.entity.IdFromPayload
import ru.vsu.front.model.entity.AuthTokens
import java.util.*

/**
 * Хранилище JWT-токенов в памяти.
 *
 * @param dispatcherProvider Провайдер корутинных диспатчеров.
 * @param json Json.
 */
class InMemoryTokenStorage(
    private val dispatcherProvider: DispatcherProvider,
    private val json: Json,
) : TokenStorage {
    var accessToken: String? = null
    var refreshToken: String? = null

    /**
     * Возвращает текущие токены.
     * * Возвращает пару [AuthTokens] в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    override fun getTokensSync(): AuthTokens? {
        val currentAccessToken = accessToken ?: return null
        val currentRefreshToken = refreshToken ?: return null
        return AuthTokens(currentAccessToken, currentRefreshToken)
    }

    /**
     * Возвращает текущие токены.
     * * Асинхронно возвращает [AuthTokens] в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    override suspend fun getTokensAsync(): AuthTokens? {
        return withContext(dispatcherProvider.default) {
            getTokensSync()
        }
    }

    /**
     * Зашифровывает и сохраняет переданный токен в локальное хранилище.
     *
     * @param token Строка-токен в не зашифрованном виде.
     * @param isAccess Тип токена: `true` - Access-токен, `false` - Refresh-токен.
     */
    override fun saveToken(token: String, isAccess: Boolean) {
        when (isAccess) {
            true -> accessToken = token
            false -> refreshToken = token
        }
    }

    /**
     * Удаляет Access и Refresh токены из хранилища.
     */
    override fun clearTokens() {
        accessToken = null
        refreshToken = null
    }

    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @return Идентификатор пользователя или null.
     */
    override fun getUserIdFromToken(): Int? {
        return try {
            val accessToken = getTokensSync()?.accessToken ?: return null
            val payloadBase64 = accessToken.split(".")[1]
            val decodedBytes = Base64.getUrlDecoder().decode(payloadBase64)
            val decodedString = String(decodedBytes, Charsets.UTF_8)
            json.decodeFromString<IdFromPayload>(decodedString).id
        } catch (_: Exception) {
            null
        }
    }
}