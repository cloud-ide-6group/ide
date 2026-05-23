package ru.vsu.front.model.entity

/**
 * Файл.
 *
 * @property id Идентификатор файла.
 * @property name Название файла.
 * @property isFolder Является ли папкой.
 * @property children Внутренние файлы (для папки).
 */
data class FileNode(
    val id: Int,
    val name: String,
    val isFolder: Boolean,
    val children: List<FileNode> = emptyList(),
)
