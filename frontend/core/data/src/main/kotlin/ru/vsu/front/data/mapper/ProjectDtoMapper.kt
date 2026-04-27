package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.ProgramingLanguageDto
import ru.vsu.front.data.entity.dto.ProjectDto
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Project


/**
 * Преобразует DTO-модель ([ProjectDto])
 * в доменную сущность пользователя ([Project]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [Project], содержащая айди и название проекта.
 */
fun ProjectDto.toEntity(): Project {
    return Project(
        id = id,
        name = name
    )
}

/**
 * Преобразует список DTO-моделей ([ProjectDto])
 * в список доменных сущностей проектов ([Project]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Список [Project], содержащий информацию о доступных языках.
 */
fun List<ProjectDto>.toEntities(): List<Project> {
    return map { it.toEntity() }
}