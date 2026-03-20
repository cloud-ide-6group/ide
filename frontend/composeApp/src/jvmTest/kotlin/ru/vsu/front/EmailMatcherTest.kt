package ru.vsu.front

import ru.vsu.front.features.auth.domain.validation.EmailMatcher
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EmailMatcherTest {

    @Test
    fun `should return false for invalid email formats`() {
        val invalidEmails = listOf(
            "dfgwer",            // Нет @ и домена
            "dgecrec@",          // Нет домена после @
            "@example.com",      // Нет имени пользователя
            "plainaddress",      // Просто текст
            "email.example.com", // Нет @
            "email@example@com", // Две @
            "email@example..com",// Две точки в домене
            "email@.com",        // Домен начинается с точки
            "email@domain.c",    // Минимум 2 символа после точки
            " ",                 // Пробел
            ""                   // Пустая строка
        )

        invalidEmails.forEach { email ->
            assertFalse(
                actual = EmailMatcher.isValid(email),
                message = "Email '$email' should be invalid"
            )
        }
    }

    @Test
    fun `should return true for valid email formats`() {
        val validEmails = listOf(
            "mail@mail.ru",
            "mail.mail@mail.ru",
            "1234567890@example.com",
            "email@subdomain.example.com",
            "email@example.corporate",
            "_______@example.com"
        )

        validEmails.forEach { email ->
            assertTrue(
                actual = EmailMatcher.isValid(email),
                message = "Email '$email' should be valid"
            )
        }
    }
}