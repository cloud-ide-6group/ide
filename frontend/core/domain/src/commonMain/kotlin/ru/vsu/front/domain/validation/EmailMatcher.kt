package ru.vsu.front.domain.validation

/**
 * Валидатор почты.
 */
object EmailMatcher {
    private val EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,12}\$".toRegex()

    /**
     * Проверяет корректность почты.
     *
     * @return true если почта корректна, false если почта некорректна
     */
    fun isValid(email: String): Boolean = email.matches(EMAIL_REGEX)
}