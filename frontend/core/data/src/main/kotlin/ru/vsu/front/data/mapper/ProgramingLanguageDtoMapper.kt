package ru.vsu.front.data.mapper

import ru.vsu.front.data.dto.ProgramingLanguageDto
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Преобразует DTO-модель ([ProgramingLanguageDto])
 * в доменную сущность пользователя ([ProgramingLanguage]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 * * @return Модель [ProgramingLanguage], содержащая информацию о доступном языке.
 */
fun ProgramingLanguageDto.toEntity(): ProgramingLanguage {
    return ProgramingLanguage(
        id = id,
        name = name,
        description = description
    )
}
fun List<ProgramingLanguageDto>.toEntities(): List<ProgramingLanguage> {
    return map { it.toEntity() }
}