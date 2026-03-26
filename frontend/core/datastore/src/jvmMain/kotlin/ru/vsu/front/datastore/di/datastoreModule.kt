package ru.vsu.front.datastore.di

import org.koin.dsl.module
import ru.vsu.front.datastore.CryptoManager
import ru.vsu.front.datastore.TokenStorage
import java.util.prefs.Preferences

/**
 * Модуль хранения данных на устройстве.
 * * Отвечает за предоставление зависимостей, связанных с криптографией
 * и безопасным сохранением токенов на устройстве.
 *
 * * Что внутри:
 * - [Preferences] - хранилище.
 * - [CryptoManager] - класс для шифрования и дешифрования локальных данных.
 * - [TokenStorage] - инструмент для записи и чтения JWT-токенов,
 * использующий [Preferences] и [CryptoManager].
 */
val datastoreModule = module {
    single {
        Preferences.userRoot().node("ru.vsu.front.keys")
    }

    single {
        CryptoManager(prefs = get())
    }

    single {
        TokenStorage(cryptoManager = get(), prefs = get())
    }
}