package ru.vsu.front.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.domain.repository.NotificationsRepository
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.File
import ru.vsu.front.model.entity.FileNode
import ru.vsu.front.model.entity.Notification
import ru.vsu.front.model.entity.Response

/**
 * UseCase для подписки на получение дерева файлов проекта.
 *
 * @property repository Интерфейс репозитория.
 */
class ObserveFilesUseCase(
    private val repository: ProjectRepository,
) {
    /**
     * Выполняет подписку на получение списка файлов проекта.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Flow] со списком файловых узлов [FileNode].
     */
    operator fun invoke(projectId: Int): Flow<List<FileNode>> {
        return repository.observeFiles(projectId)
    }
}
