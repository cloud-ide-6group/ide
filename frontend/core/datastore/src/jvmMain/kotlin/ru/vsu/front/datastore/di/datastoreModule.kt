package ru.vsu.front.datastore.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import org.koin.dsl.module
import ru.vsu.front.datastore.CryptoManager
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.datastore.getAppPreferencesDirectory
import java.io.File
/**
 * Модуль хранения данных на устройстве.
 * * Отвечает за предоставление зависимостей, связанных с криптографией
 * и безопасным сохранением токенов на устройстве.
 *
 * * Что внутри:
 * - [ru.vsu.front.datastore.CryptoManager] - класс для шифрования и дешифрования локальных данных.
 * - `DataStore<Preferences>` — файловое хранилище токенов.
 * - [TokenStorage] - сервис для безопасной записи и чтения JWT-токенов,
 * использующий предоставленные DataStore и CryptoManager.
 */
val datastoreModule = module {
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