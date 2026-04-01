package ru.vsu.front.model.entity

/**
 *
 *
 *
 */
data class ProgramingLanguage(
    val id: Int,
    val name: String,
    val description: String,
    val imageName: String
)

val defaultProgramingLanguages = buildList {
    add(ProgramingLanguage(
        id = 1,
        name = "Java",
        description = "",
        imageName = "Java-17"
    ))
    add(ProgramingLanguage(
        id = 2,
        name = "C++",
        description = "",
        imageName = "C++"
    ))
}