package ru.vsu.front.datastore

import java.util.*
import java.util.prefs.Preferences
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Класс для шифрования и дешифрования локальных данных.
 * * Использует алгоритм AES (256 бит). Мастер-ключ генерируется автоматически при первом запуске
 * и сохраняется в хранилище через [Preferences].
 * * @param prefs Хранилище.
 */
class CryptoManager(
    private val prefs: Preferences
) {
    private val keyName = "aes_master_key"
    private val secretKey: SecretKey

    init {
        val encodedKey = prefs.get(keyName, null)
        if (encodedKey == null) {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256)
            secretKey = keyGen.generateKey()

            prefs.put(keyName, Base64.getEncoder().encodeToString(secretKey.encoded))
        } else {
            val decodedKey = Base64.getDecoder().decode(encodedKey)
            secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        }
    }

    /**
     * Зашифровывает переданную строку.
     *
     * @param plainText Исходная незашифрованная строка.
     * @return Зашифрованная строка, закодированная в формате Base64.
     */
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    /**
     * Расшифровывает зашифрованную строку.
     *
     * @param encryptedText Зашифрованная строка в формате Base64, полученная из метода [encrypt].
     * @return Расшифрованная исходная строка или `null`, если произошла ошибка.
     */
    fun decrypt(encryptedText: String): String? {
        return try {
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decodedBytes = Base64.getDecoder().decode(encryptedText)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}