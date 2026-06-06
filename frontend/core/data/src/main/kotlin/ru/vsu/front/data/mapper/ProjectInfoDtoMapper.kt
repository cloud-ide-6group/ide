package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.ProjectInfoDto
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.ProjectInfo

/**
 * Преобразует DTO-модель ([ProjectInfoDto])
 * в доменную сущность языка программирования ([ProjectInfo]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [ProgramingLanguage], содержащая информацию о доступном языке.
 */
fun ProjectInfoDto.toEntity(): ProjectInfo {
    return ProjectInfo(
        languageName = languageName,
        projectId = projectId,
        projectName = projectName,
        isUserOwner = isUserOwner,
        users = users.toEntities()
    )
}