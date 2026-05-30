package ru.vsu.front.model.entity

/**
 * Содержимое файла.
 *
 * @property id Идентификатор файла.
 * @property content Содержимое файла (строка).
 */
data class FileContent(
    val id: Int,
    val content: String
)
