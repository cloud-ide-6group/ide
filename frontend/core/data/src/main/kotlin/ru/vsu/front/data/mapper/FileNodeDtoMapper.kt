package ru.vsu.front.data.mapper

import ru.vsu.front.data.entity.dto.FileNodeDto
import ru.vsu.front.model.entity.FileNode

/**
 * Преобразует DTO-модель ([FileNodeDto])
 * в доменную сущность пользователя ([FileNode]).
 * * * Изолирует модели бизнес-логики от аннотаций `@Serializable` и `@SerialName`.
 *
 * * @return Модель [FileNode], содержащая токены.
 */
fun FileNodeDto.toEntity(): FileNode {
    return FileNode(
        id = id,
        name = name,
        isFolder = isFolder,
        children = children.map {
            it.toEntity()
        }
    )
}