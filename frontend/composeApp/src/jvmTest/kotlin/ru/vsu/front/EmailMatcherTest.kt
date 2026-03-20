package ru.vsu.front

import ru.vsu.front.features.auth.domain.validation.EmailMatcher
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppDesktopTest {

    @Test
    fun `if email is dfgwer it's false`() {
        assertEquals(false, EmailMatcher.isValid("dfgwer"))
    }

    @Test
    fun `if email is dgecrec@ it's false`() {
        assertEquals(false, EmailMatcher.isValid("dgecrec@"))
    }

    @Test
    fun `if email is valid it's true`() {
        assertEquals(true, EmailMatcher.isValid("mail@mail.ru"))
        assertEquals(true, EmailMatcher.isValid("mail.mail@mail.ru"))
    }
}