package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response

interface ProjectRepository {

    /**
     * Выполняет создание проекта.

     * @return [Response] с идентификатором проекта, либо с ошибкой.
     */
    suspend fun createProject(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int>
}