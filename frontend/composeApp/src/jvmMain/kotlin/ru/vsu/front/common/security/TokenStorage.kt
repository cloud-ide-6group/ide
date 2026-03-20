package ru.vsu.front.common.security

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Хранилище JWT-токенов на основе DataStore.
 * Обеспечивает безопасное чтение, запись и удаление токенов с использованием локального шифрования.
 *
 * @param dataStore Файловое хранилище настроек из библиотеки `datastore-preferences`.
 * @param cryptoManager Утилита для шифрования и дешифрования строковых данных.
 */
class TokenStorage(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) {
    companion object {
        private val JWT_ACCESS_TOKEN_KEY = stringPreferencesKey("jwt_access_token")
        private val JWT_REFRESH_TOKEN_KEY = stringPreferencesKey("jwt_refresh_token")
    }

    /**
     * Горячий поток, эмитящий текущее состояние токенов из хранилища.
     * * Возвращает пару `Pair(AccessToken, RefreshToken)` в расшифрованном виде.
     * * Если хотя бы одного токена нет в хранилище, эмиттит `null`.
     */
    val tokenFlow: Flow<Pair<String?, String?>?> = dataStore.data.map { preferences ->
        val encryptedAccessToken = preferences[JWT_ACCESS_TOKEN_KEY]
        val encryptedRefreshToken = preferences[JWT_REFRESH_TOKEN_KEY]
        if (encryptedAccessToken != null && encryptedRefreshToken != null) {
            with(cryptoManager) {
                decrypt(encryptedAccessToken) to decrypt(encryptedRefreshToken)
            }
        } else {
            null
        }
    }

    /**
     * Зашифровывает и сохраняет переданный токен в локальное хранилище.
     *
     * @param token Строка-токен в не зашифрованном виде.
     * @param isAccess Тип токена: `true` - Access-токен, `false` - Refresh-токен.
     */
    suspend fun saveToken(token: String, isAccess: Boolean) {
        val encryptedToken = cryptoManager.encrypt(token)
        dataStore.edit { preferences ->
            if (isAccess) {
                preferences[JWT_ACCESS_TOKEN_KEY] = encryptedToken

            } else {
                preferences[JWT_REFRESH_TOKEN_KEY] = encryptedToken
            }
        }
    }

    /**
     * Удаляет Access и Refresh токены из хранилища.
     */
    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(JWT_ACCESS_TOKEN_KEY)
            preferences.remove(JWT_REFRESH_TOKEN_KEY)
        }
    }
}