package ru.vsu.front.features.auth.domain.validation

/**
 * Валидатор почты.
 */
object EmailMatcher {
    private val EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,12}\$".toRegex()

    fun isValid(email: String): Boolean = email.matches(EMAIL_REGEX)
}