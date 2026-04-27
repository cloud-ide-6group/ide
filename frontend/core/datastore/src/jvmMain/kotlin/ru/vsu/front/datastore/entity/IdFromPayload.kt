package ru.vsu.front.datastore.entity

import kotlinx.serialization.Serializable

/**
 * Сущность, хранящая идентификатор пользователя, извлеченный из Payload токена доступа
 *
 * @property id Идентификатор пользователя.
 */
@Serializable
data class IdFromPayload(val id: Int)