package ru.vsu.front.data.entity.dto

/**
 * DTO файла.
 *
 * @property id Идентификатор файла.
 * @property name Название файла.
 * @property isFolder Является ли папкой
 * @property children Внутренние файлы (для папки).
 */
data class FileNodeDto(
    val id: Int,
    val name: String,
    val isFolder: Boolean,
    val children: List<FileNodeDto> = emptyList(),
)
