package ru.vsu.front.model.entity

/**
 * Язык программирования.
 *
 * @property id Идентификатор языка программирования.
 * @property name Название языка программирования.
 * @property description Описание языка программирования.
 */
data class ProgramingLanguage(
    val id: Int,
    val name: String,
    val description: String
)