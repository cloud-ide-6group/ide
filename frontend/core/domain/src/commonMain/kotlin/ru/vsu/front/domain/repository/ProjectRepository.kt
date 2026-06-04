package ru.vsu.front.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.vsu.front.model.entity.ConsoleOutput
import ru.vsu.front.model.entity.FileContent
import ru.vsu.front.model.entity.FileNode
import ru.vsu.front.model.entity.Message
import ru.vsu.front.model.entity.ProjectInfo
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User

/**
 * Интерфейс репозитория для работы с проектами.
 */
interface ProjectRepository {

    /**
     * Выполняет создание проекта.
     *
     * @param programingLanguageId Идентификатор языка программирования.
     * @param projectName Название проекта.
     *
     * @return [Response] с идентификатором проекта, либо с ошибкой.
     */
    suspend fun createProject(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int>

    /**
     * Выполняет получение информации о проекте и статусе пользователя.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с информацией о проекте, либо с ошибкой.
     */
    suspend fun getProjectInfo(
        projectId: Int
    ): Response<ProjectInfo>

    /**
     * Выполняет удаление проекта.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с не важно чем (важен только код ответа), либо с ошибкой.
     */
    suspend fun deleteProject(
        projectId: Int
    ): Response<*>

    /**
     * Выполняет исключение участника из проекта.
     *
     * @param userEmail Почта участника.
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с не важно чем (важен только код ответа), либо с ошибкой.
     */
    suspend fun kickUser(
        userEmail: String,
        projectId: Int
    ): Response<*>

    /**
     * Выполняет приглашение пользователя в проект.
     *
     * @param userEmail Почта пользователя.
     * @param projectId Идентификатор проекта.
     *
     * @return [Response] с не важно чем (важен только код ответа), либо с ошибкой.
     */
    suspend fun inviteUser(
        userEmail: String,
        projectId: Int
    ): Response<User>

    /**
     * Отправляет обновленное содержимое файла на сервер
     *
     * @param fileId Идентификатор файла.
     * @param content Новое содержимое файла.
     */
    suspend fun updateFileContent(fileId: Int, content: String)

    /**
     * Выполняет подписку на получение файлов проекта.
     *
     * @param projectId Идентификатор проекта.
     *
     * @return [Flow] со списком текущих файлов.
     */
    fun observeFiles(projectId: Int): Flow<List<FileNode>>

    /**
     * Выполняет получение содержимого файла (не папки).
     *
     * @return [Flow] с текущим содержимым файла.
     */
    fun observeFileContent(): Flow<FileContent>

    /**
     * Подписка на поток вывода консоли для запущенного проекта.
     *
     * @param projectId Идентификатор отслеживаемого проекта.
     *
     * @return [Flow] со строками вывода консоли приложения.
     */
    fun observeConsoleOutput(projectId: Int): Flow<ConsoleOutput>

    /**
     * Подписка на сообщения текущего открытого чата.
     *
     * @return [Flow] с идентификатором чата и списком его сообщений.
     */
    fun observeMessages(): Flow<Pair<Int, List<Message>>>

    /**
     * Подписка на исключение текущего пользователя из проекта.
     *
     * @return [Flow] с идентификатором проекта, из которого был удален пользователь.
     */
    fun observeRemovedFromProject(): Flow<Int>

    /**
     * Запускает код текущего проекта.
     *
     * @param projectId Идентификатор запускаемого проекта.
     */
    suspend fun runCode(projectId: Int)

    /**
     * Останавливает выполнение кода текущего проекта.
     *
     * @param projectId Идентификатор останавливаемого проекта.
     */
    suspend fun stopCode(projectId: Int)

    /**
     * Выполняет подключение к комнате проекта
     *
     * @param projectId Идентификатор проекта.
     */
    suspend fun connectToTheProjectRoom(projectId: Int)

    /**
     * Закрывает соединение с комнатой проекта, отписывается от всех событий и отключает сокет.
     */
    suspend fun leaveFromProjectRoom(projectId: Int)

    /**
     * Подключает пользователя к чату.
     *
     * @param identificator Строковый идентификатор чата.
     * @param projectId Идентификатор проекта.
     */
    suspend fun joinChatRoom(identificator: String, projectId: Int)

    /**
     * Отключает пользователя от чата.
     *
     * @param identificator Строковый идентификатор чата.
     * @param projectId Идентификатор проекта.
     */
    suspend fun leaveChatRoom(identificator: String, projectId: Int)

    /**
     * Отправляет ввод в терминал выполняемой программы.
     *
     * @param input Ввод строка.
     * @param projectId Идентификатор проекта.
     */
    suspend fun sendInput(input: String, projectId: Int)

    /**
     * Получение содержимого определенного файла.
     */
    suspend fun getFileContent(fileId: Int)
}