package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.ProgramingLanguageDto
import ru.vsu.front.model.entity.ProgramingLanguage

/**
 * Преобразует DTO-модель ([ProgramingLanguageDto])
 * в доменную сущность языка программирования ([ProgramingLanguage]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [ProgramingLanguage], содержащая информацию о доступном языке.
 */
fun ProgramingLanguageDto.toEntity(): ProgramingLanguage {
    return ProgramingLanguage(
        id = id,
        name = name,
        description = description
    )
}

/**
 * Преобразует список DTO-моделей ([ProgramingLanguageDto])
 * в список доменных сущностей языков программирования ([ProgramingLanguage]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Список [ProgramingLanguage], содержащий информацию о доступных языках.
 */
fun List<ProgramingLanguageDto>.toEntities(): List<ProgramingLanguage> {
    return map { it.toEntity() }
}