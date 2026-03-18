package ru.vsu.front.features.auth.domain.validation

object EmailMatcher {
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()

    fun isValid(email: String): Boolean = email.matches(EMAIL_REGEX)
}