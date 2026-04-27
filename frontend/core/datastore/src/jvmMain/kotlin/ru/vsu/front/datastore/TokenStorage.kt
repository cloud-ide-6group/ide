package ru.vsu.front.datastore

import kotlinx.serialization.json.Json
import ru.vsu.front.datastore.entity.IdFromPayload
import ru.vsu.front.model.entity.AuthTokens
import java.util.Base64
import java.util.prefs.Preferences

/**
 * Хранилище JWT-токенов.
 * Обеспечивает безопасное чтение, запись и удаление токенов с использованием локального шифрования.
 *
 * @param cryptoManager Утилита для шифрования и дешифрования.
 * @param prefs Хранилище.
 * @param json Json.
 */
class TokenStorage(
    private val cryptoManager: CryptoManager,
    private val prefs: Preferences,
    private val json: Json
) {
    companion object {
        private const val JWT_ACCESS_TOKEN_KEY = "jwt_access_token"
        private const val JWT_REFRESH_TOKEN_KEY = "jwt_refresh_token"
    }

    /**
     * Возвращает текущие токены.
     * * Возвращает пару [AuthTokens] в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    fun getTokensSync(): AuthTokens? {
        val encryptedAccessToken = prefs.get(JWT_ACCESS_TOKEN_KEY, null)
        val encryptedRefreshToken = prefs.get(JWT_REFRESH_TOKEN_KEY, null)

        if (encryptedAccessToken != null && encryptedRefreshToken != null) {

            val decodedAccessToken = cryptoManager.decrypt(encryptedAccessToken)
            val decodedRefreshToken = cryptoManager.decrypt(encryptedRefreshToken)

            if (decodedAccessToken != null && decodedRefreshToken != null) {
                return AuthTokens(decodedAccessToken, decodedRefreshToken)
            }
        }
        return null
    }

    /**
     * Возвращает текущие токены.
     * * Асинхронно возвращает [AuthTokens] в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    suspend fun getTokens(): AuthTokens? {
        return getTokensSync()
    }

    /**
     * Зашифровывает и сохраняет переданный токен в локальное хранилище.
     *
     * @param token Строка-токен в не зашифрованном виде.
     * @param isAccess Тип токена: `true` - Access-токен, `false` - Refresh-токен.
     */
    suspend fun saveToken(token: String, isAccess: Boolean) {
        val encryptedToken = cryptoManager.encrypt(token)

        if (isAccess) {
            prefs.put(JWT_ACCESS_TOKEN_KEY, encryptedToken)
        } else {
            prefs.put(JWT_REFRESH_TOKEN_KEY, encryptedToken)
        }
    }

    /**
     * Удаляет Access и Refresh токены из хранилища.
     */
    fun clearTokens() {
        prefs.remove(JWT_ACCESS_TOKEN_KEY)
        prefs.remove(JWT_REFRESH_TOKEN_KEY)
    }

    /**
     * Извлекает идентификатор пользователя из токена.
     *
     * @return Идентификатор пользователя или null.
     */
    fun getUserIdFromToken(): Int? {
        return try {
            val accessToken = getTokensSync()?.accessToken ?: return null
            val payloadBase64 = accessToken.split(".")[1]
            val decodedBytes = Base64.getUrlDecoder().decode(payloadBase64)
            val decodedString = String(decodedBytes, Charsets.UTF_8)
            println(accessToken)
            json.decodeFromString<IdFromPayload>(decodedString).id
        } catch (_: Exception) {
            null
        }
    }
}