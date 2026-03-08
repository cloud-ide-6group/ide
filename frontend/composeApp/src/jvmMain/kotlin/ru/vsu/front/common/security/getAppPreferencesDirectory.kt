package ru.vsu.front.common.security

import ru.vsu.front.common.Const.APP_NAME
import java.io.File

/**
 * Возвращает путь для хранения данных
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