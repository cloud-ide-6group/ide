package ru.vsu.front.common.security

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Хранилище токенов
 *
 * @param dataStore Хранилище из библиотеки datastore.preferences
 * @param cryptoManager Класс для работы с шифрованием
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
     * Подписка на изменение ключей в хранилище
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
     * @param token Токен для сохранения
     * @param isAccess True, если это access токен | False, если это refresh токен
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
     * Удаление токенов из хранилища
     */
    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(JWT_ACCESS_TOKEN_KEY)
            preferences.remove(JWT_REFRESH_TOKEN_KEY)
        }
    }
}