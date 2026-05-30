package ru.vsu.front.model.entity

/**
 * Вывод программы.
 *
 * @property text Текстовый вывод программы.
 * @property isProgramEnded Закончено ли выполнение программы.
 */
data class ConsoleOutput(
    val text: String,
    val isProgramEnded: Boolean
)
