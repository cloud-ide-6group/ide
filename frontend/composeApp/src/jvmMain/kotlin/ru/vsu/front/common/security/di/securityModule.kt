package ru.vsu.front.common.security.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import org.koin.dsl.module
import ru.vsu.front.common.security.CryptoManager
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.common.security.getAppPreferencesDirectory
import java.io.File

/**
 * Koin-модуль безопасности и локального хранения данных.
 * * Отвечает за предоставление зависимостей, связанных с криптографией
 * и безопасным сохранением токенов на устройстве.
 * * Предоставляет:
 * - [CryptoManager] - класс для шифрования и дешифрования локальных данных.
 * - `DataStore<Preferences>` — файловое хранилище токенов.
 * - [TokenStorage] - сервис для безопасной записи и чтения JWT-токенов,
 * использующий предоставленные DataStore и CryptoManager.
 */
val securityModule = module {

    single {
        CryptoManager()
    }

    single {
        PreferenceDataStoreFactory.create(
            produceFile = {
                val appDir = getAppPreferencesDirectory()
                File(appDir, "auth.preferences_pb")
            }
        )
    }

    single {
        TokenStorage(dataStore = get(), cryptoManager = get())
    }
}