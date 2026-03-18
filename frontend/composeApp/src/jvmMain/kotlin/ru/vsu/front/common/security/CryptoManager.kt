package ru.vsu.front.common.security

import java.util.*
import java.util.prefs.Preferences
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * @see prefs Хранилище
 * @see keyName Название ключа
 * @see secretKey Секретный ключ
 */
class CryptoManager {
    private val prefs = Preferences.userRoot().node("ru.vsu.front.keys")
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
     * Шифровка строки
     *
     * @param plainText Строка
     */
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    /**
     * Расшифровка строки
     *
     * @param encryptedText Зашифрованная строка
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