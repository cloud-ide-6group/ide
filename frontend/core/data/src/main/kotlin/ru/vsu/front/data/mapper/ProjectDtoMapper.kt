package ru.vsu.front.data.mapper

import ru.vsu.front.data.dto.ProjectDto
import ru.vsu.front.model.entity.Project


/**
 * Преобразует DTO-модель ([ProjectDto])
 * в доменную сущность пользователя ([Project]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 * * @return Модель [Project], содержащая айди и название проекта.
 */
fun ProjectDto.toEntity(): Project {
    return Project(
        id = id,
        name = name
    )
}

fun List<ProjectDto>.toEntities(): List<Project> {
    return map { it.toEntity() }
}