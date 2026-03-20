package ru.vsu.front.common.security

import ru.vsu.front.common.Const.APP_NAME
import java.io.File

/**
 * Возвращает системный путь к директории для хранения локальных файлов приложения.
 * * Поддерживает только Windows.
 * * Если директория не существует, она будет создана автоматически.
 *
 * @return Объект [File], рабочая директория приложения.
 * @throws IllegalStateException Если текущая операционная система не поддерживается.
 */
fun getAppPreferencesDirectory(): File {
    val os = System.getProperty("os.name").lowercase()

    val appDir = when {
        os.contains("win") -> File(System.getenv("APPDATA"), APP_NAME)
        else -> error("Only windows supported")
    }

    if (!appDir.exists()) {
        appDir.mkdirs()
    }

    return appDir
}