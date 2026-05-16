package ru.vsu.front.model.entity

/**
 * Файл.
 *
 * @property id Идентификатор файла.
 * @property name Название файла.
 * @property parentId Идентификатор пакета, в котором лежит файл.
 * @property projectId Идентификатор проекта, которому принадлежит файл.
 * @property isFolder Является ли папкой.
 */
data class File(
    val id: Int,
    val name: String,
    val parentId: Int?,
    val projectId: Int,
    val isFolder: Boolean
)