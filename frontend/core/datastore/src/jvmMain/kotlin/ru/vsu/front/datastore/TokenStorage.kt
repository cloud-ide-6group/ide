package ru.vsu.front.datastore

import java.util.prefs.Preferences

/**
 * Хранилище JWT-токенов.
 * Обеспечивает безопасное чтение, запись и удаление токенов с использованием локального шифрования.
 *
 * @param cryptoManager Утилита для шифрования и дешифрования строковых данных.
 * @param prefs Хранилище.
 */
class TokenStorage(
    private val cryptoManager: CryptoManager,
    private val prefs: Preferences
) {
    companion object {
        private const val JWT_ACCESS_TOKEN_KEY = "jwt_access_token"
        private const val JWT_REFRESH_TOKEN_KEY = "jwt_refresh_token"
    }


    private fun getTokensSync(): Pair<String?, String?>? {
        val encryptedAccessToken = prefs.get(JWT_ACCESS_TOKEN_KEY, null)
        val encryptedRefreshToken = prefs.get(JWT_REFRESH_TOKEN_KEY, null)

        return if (encryptedAccessToken != null && encryptedRefreshToken != null) {
            with(cryptoManager) {
                decrypt(encryptedAccessToken) to decrypt(encryptedRefreshToken)
            }
        } else {
            null
        }
    }

    /**
     * Возвращает текущие токены.
     * * Возвращает пару `Pair(AccessToken, RefreshToken)` в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, возвращает `null`.
     */
    suspend fun getTokens(): Pair<String?, String?>? {
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
    suspend fun clearTokens() {
        prefs.remove(JWT_ACCESS_TOKEN_KEY)
        prefs.remove(JWT_REFRESH_TOKEN_KEY)
    }
}