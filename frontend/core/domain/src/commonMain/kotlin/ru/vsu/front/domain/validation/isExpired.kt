package ru.vsu.front.domain.validation

/**
 *
 *
 */
fun Long.isExpired(): Boolean {
    return System.currentTimeMillis() > this
}
