package ru.vsu.front.model.entity

/**
 * Файл.
 *
 * @property name Название файла.
 * @property parentId Идентификатор пакета, в котором лежит файл.
 * @property projectId Идентификатор проекта, которому принадлежит файл.
 * @property isFolder Является ли папкой.
 */
data class File(
    val name: String,
    val parentId: Int?,
    val projectId: Int,
    val isFolder: Boolean
)