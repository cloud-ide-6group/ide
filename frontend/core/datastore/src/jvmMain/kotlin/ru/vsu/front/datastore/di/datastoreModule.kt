package ru.vsu.front.datastore.di

import kotlinx.serialization.json.Json
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.vsu.front.datastore.CryptoManager
import ru.vsu.front.datastore.token_storage.DeviceTokenStorage
import ru.vsu.front.datastore.token_storage.InMemoryTokenStorage
import ru.vsu.front.datastore.token_storage.TokenStorage
import java.util.prefs.Preferences

/**
 * Модуль хранения данных на устройстве.
 * * Отвечает за предоставление зависимостей, связанных с криптографией
 * и безопасным сохранением токенов на устройстве.
 *
 * * Что внутри:
 * - [Preferences] - хранилище.
 * - [CryptoManager] - класс для шифрования и дешифрования локальных данных.
 * - [Json] - Json.
 * - [TokenStorage] - инструмент для записи и чтения JWT-токенов [InMemoryTokenStorage] или [DeviceTokenStorage].
 */
val datastoreModule = module {
    single {
        Preferences.userRoot().node("ru.vsu.front.keys")
    }

    single {
        CryptoManager(get())
    }

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single {
        InMemoryTokenStorage(get(), get())
    }.bind<TokenStorage>()
}