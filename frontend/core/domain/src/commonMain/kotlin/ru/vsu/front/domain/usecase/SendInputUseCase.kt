package ru.vsu.front.domain.usecase

import ru.vsu.front.domain.repository.ProjectRepository

/**
 * UseCase для отправки ввода (строки) в выполняемую программу.
 *
 * @property repository Интерфейс репозитория.
 */
class SendInputUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет отправку ввода (строки) в выполняемую программу.
     *
     * @param input Ввод (строка).
     * @param projectId Идентификатор проекта.
     */
    suspend operator fun invoke(
        input: String,
        projectId: Int
    ) {
        return repository.sendInput(input, projectId)
    }
}
