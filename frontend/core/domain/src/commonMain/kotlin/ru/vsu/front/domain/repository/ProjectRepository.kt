package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response

interface ProjectRepository {

    /**
     * Выполняет создание проекта.

     * @return [Response] с результатом создания проекта, 201 или другие ошибки.
     */
    suspend fun createProject(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int>
}