package ru.vsu.front.common.security.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import org.koin.dsl.module
import ru.vsu.front.common.security.CryptoManager
import ru.vsu.front.common.security.TokenStorage
import ru.vsu.front.common.security.getAppPreferencesDirectory
import java.io.File

/**
 * Модуль безопасности
 *
 * @see CryptoManager Предоставление класса шифрования
 * @see PreferenceDataStoreFactory Предоставление хранилища из библиотеки datastore.preferences
 * @see TokenStorage Предоставление хранилища токенов
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