package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProgramingLanguageRepository
import ru.vsu.front.model.entity.ProgramingLanguage
import ru.vsu.front.model.entity.Response

/**
 * UseCase для получения доступных языков программирования.
 *
 * @property repository Интерфейс репозитория.
 *
 */
class GetProgramingLanguagesUseCase(
    private val repository: ProgramingLanguageRepository,
) {
    /**
     * Выполняет получение доступных языков программирования.
     *
     * @return [Response] с доступными языками при успехе, либо с ошибкой.
     */
    suspend operator fun invoke(): Response<List<ProgramingLanguage>> {
        return repository.getProgramingLanguages()
    }
}
