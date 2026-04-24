package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Response

/**
 * Интерфейс репозитория, связанного с языками программирования.
 */
interface ProgramingLanguageRepository {

    /**
     * Выполняет получение доступных языков программирования.

     * @return [Response] с доступными языками программирования, либо с ошибкой.
     */
    suspend fun getProgramingLanguages(): Response<List<ProgramingLanguage>>
}