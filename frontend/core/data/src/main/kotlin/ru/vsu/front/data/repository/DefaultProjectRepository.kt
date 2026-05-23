package ru.vsu.front.data.repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import jdk.internal.net.http.common.Utils.close
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONArray
import org.json.JSONObject
import ru.vsu.front.data.entity.dto.ErrorResponseDto
import ru.vsu.front.data.entity.dto.ProjectInfoDto
import ru.vsu.front.data.entity.request.CreateProjectRequest
import ru.vsu.front.data.entity.request.DeleteProjectRequest
import ru.vsu.front.data.entity.request.GetProjectInfoRequest
import ru.vsu.front.data.entity.request.InviteUserRequest
import ru.vsu.front.data.entity.request.KickUserRequest
import ru.vsu.front.data.entity.response.CreateProjectResponse
import ru.vsu.front.data.entity.response.ProjectInfoResponse
import ru.vsu.front.data.mapper.toEntity
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.ProjectRepository
import ru.vsu.front.model.entity.FileNode
import ru.vsu.front.model.entity.Message
import ru.vsu.front.model.entity.ProjectInfo
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.network.HttpRoutes.CREATE_PROJECT
import ru.vsu.front.network.HttpRoutes.DELETE_PROJECT
import ru.vsu.front.network.HttpRoutes.GET_PROJECT_INFO
import ru.vsu.front.network.HttpRoutes.INVITE_USER
import ru.vsu.front.network.HttpRoutes.KICK_USER
import ru.vsu.front.network.MainHttpClientManager
import ru.vsu.front.network.SocketRoutes.CONSOLE_OUTPUT
import ru.vsu.front.network.SocketRoutes.FILES_TREES_LIST
import ru.vsu.front.network.SocketRoutes.GET_FILE_CONTENT
import ru.vsu.front.network.SocketRoutes.GET_MESSAGES
import ru.vsu.front.network.SocketRoutes.JOIN_CHAT_ROOM
import ru.vsu.front.network.SocketRoutes.JOIN_PROJECT_ROOM
import ru.vsu.front.network.SocketRoutes.LEAVE_CHAT_ROOM
import ru.vsu.front.network.SocketRoutes.LEAVE_PROJECT_ROOM
import ru.vsu.front.network.SocketRoutes.REMOVED_FROM_PROJECT
import ru.vsu.front.network.SocketRoutes.RUN_CODE
import ru.vsu.front.network.SocketRoutes.SEND_FILE_CONTENT
import ru.vsu.front.network.SocketRoutes.SEND_INPUT
import ru.vsu.front.network.SocketRoutes.STOP_CODE
import ru.vsu.front.network.SocketRoutes.UPDATE_FILE_CONTENT

/**
 * Реализация интерфейса [ProjectRepository] для работы с сетевым API.
 *
 * @property mainHttpClientManager Менеджер для получения HttpClient работающего с токенами.
 * @property tokenStorage Хранилище токенов.
 * @property baseUrl Базовый url.
 */
class DefaultProjectRepository(
    private val mainHttpClientManager: MainHttpClientManager,
    private val tokenStorage: TokenStorage,
    private val baseUrl: String,
) : ProjectRepository {
    private var socket: Socket? = null
    private var activeToken: String? = null
    /**
     * Выполняет POST-запрос на эндпоинт создания проекта ([CREATE_PROJECT]).
     *
     * @return [Response.Success] с доступными языками программирования при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.Forbidden] при неверных учетных данных (403).
     * @return [RequestError.Conflict] при ошибке создания проекта (409).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun createProject(
        programingLanguageId: Int,
        projectName: String
    ): Response<Int> {
        return try {
            val response = mainHttpClientManager.getClient().post(CREATE_PROJECT) {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateProjectRequest(
                        programingLanguageId = programingLanguageId,
                        projectName = projectName
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    val createdProjectId = response.body<CreateProjectResponse>().projectId
                    Response.Success(createdProjectId)
                }
                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error(RequestError.NetworkException())
        }
    }

    /**
     * Выполняет GET-запрос на эндпоинт получения информации о проекте ([GET_PROJECT_INFO]).
     *
     * @return [Response.Success] с доступными языками программирования при успешном запросе.
     * @return [RequestError.Unauthorized] при недействительном токене обновления (401).
     * @return [RequestError.Forbidden] при неверных учетных данных (403).
     * @return [RequestError.Conflict] при ошибке создания проекта (409).
     * @return [RequestError.UnknownError] при непредвиденной ошибке.
     * @return [RequestError.NetworkException] при ошибке сети.
     */
    override suspend fun getProjectInfo(projectId: Int): Response<ProjectInfo> {
       return try {
           val response = mainHttpClientManager.getClient().get(GET_PROJECT_INFO) {
               parameter("project_id", projectId)
           }

           when (response.status) {
               HttpStatusCode.OK -> {
                   val projectInfoDto = response.body<ProjectInfoResponse>().projectInfoDto
                   Response.Success(projectInfoDto.toEntity())
               }

               HttpStatusCode.Forbidden,
               HttpStatusCode.Conflict -> {
                   val message = response.body<ErrorResponseDto>().message
                   Response.Error(RequestError.Conflict(message))
               }

               else -> {
                   Response.Error(RequestError.UnknownError())
               }
           }
       } catch (_: Exception) {
           Response.Error(RequestError.NetworkException())
       }
    }

    override suspend fun deleteProject(projectId: Int): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().delete(DELETE_PROJECT) {
                contentType(ContentType.Application.Json)
                setBody(
                    DeleteProjectRequest(
                        projectId = projectId
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    override suspend fun kickUser(
        userEmail: String,
        projectId: Int
    ): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().delete(KICK_USER) {
                contentType(ContentType.Application.Json)
                setBody(
                    KickUserRequest(
                        userEmail = userEmail,
                        projectId = projectId
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    override suspend fun inviteUser(
        userEmail: String,
        projectName: String
    ): Response<*> {
        return try {
            val response = mainHttpClientManager.getClient().delete(INVITE_USER) {
                contentType(ContentType.Application.Json)
                setBody(
                    InviteUserRequest(
                        userEmail = userEmail,
                        projectName = projectName
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Response.Success(Unit)
                }

                HttpStatusCode.Forbidden,
                HttpStatusCode.Conflict -> {
                    val message = response.body<ErrorResponseDto>().message
                    Response.Error(RequestError.Conflict(message))
                }

                else -> {
                    Response.Error(RequestError.UnknownError())
                }
            }
        } catch (_: Exception) {
            Response.Error<RequestError>(RequestError.NetworkException())
        }
    }

    override fun observeFiles(projectId: Int): Flow<List<FileNode>> = callbackFlow {
        val currentSocket = getConnectedSocket()
        if (currentSocket == null) {
            close(Exception("Token is null or socket failed"))
            return@callbackFlow
        }

        currentSocket.on(FILES_TREES_LIST) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject

                if (data == null) {
                    return@on
                }

                val filesArray = data.optJSONArray(FILES_TREES_LIST) ?: return@on
                val filesList = parseJSONArrayOfFileNodes(filesArray)

                trySend(filesList)
            } catch (_: Exception) {
            }
        }

        awaitClose {
            currentSocket.off(FILES_TREES_LIST)
        }
    }

    private suspend fun getConnectedSocket(): Socket? {
        val tokens = tokenStorage.getTokens()

        if (tokens == null) {
            closeSocket()
            return null
        }

        if (socket != null && activeToken == tokens.accessToken) {
            return socket
        }

        closeSocket()

        val options = IO.Options().apply {
            auth = mapOf("token" to tokens.accessToken)
            transports = arrayOf(Polling.NAME)
        }

        activeToken = tokens.accessToken

        socket = IO.socket(baseUrl, options).apply {
            connect()
        }

        return socket
    }

    private fun parseJSONArrayOfFileNodes(fileNodes: JSONArray): List<FileNode> {
        val filesList = mutableListOf<FileNode>()

        for (i in 0 until fileNodes.length()) {
            val item = fileNodes.getJSONObject(i)

            val id = item.optInt("id", 0)
            val children = item.optJSONArray("children")
            val name = item.optString("name", null)
            val isFolder = item.optBoolean("is_folder", false)
            val fileNode = FileNode(
                id = id,
                name = name,
                isFolder = isFolder,
                children = parseJSONArrayOfFileNodes(children)
            )
            filesList.add(fileNode)
        }
        return filesList
    }

    override suspend fun connectToTheProjectRoom(projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("project_id", projectId)
        }

        currentSocket.emit(JOIN_PROJECT_ROOM, payload)
    }

    override fun observeFileContent(fileId: Int): Flow<String> = callbackFlow {
        val currentSocket = getConnectedSocket()
        if (currentSocket == null) {
            close(Exception("Token is null or socket failed"))
            return@callbackFlow
        }

        currentSocket.on(SEND_FILE_CONTENT) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject

                if (data == null) {
                    return@on
                }

                val code = data.optString("content", "")

                trySend(code)
            } catch (_: Exception) {
            }
        }

        val requestData = JSONObject().apply {
            put("file_id", fileId)
        }

        currentSocket.emit(GET_FILE_CONTENT, requestData)

        awaitClose {
            currentSocket.off(SEND_FILE_CONTENT)
        }
    }

    /**
     * Отправляет обновленный текст файла на сервер для применения изменений.
     *
     * @param fileId Идентификатор изменяемого файла.
     * @param content Новое текстовое содержимое файла.
     */
    override suspend fun updateFileContent(fileId: Int, content: String) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("file_id", fileId)
            put("content", content)
        }

        currentSocket.emit(UPDATE_FILE_CONTENT, payload)
    }

    /**
    * Отправляет команду на сервер через веб-сокет для компиляции и запуска кода текущего проекта.
    *
    * @param projectId Идентификатор запускаемого проекта.
    */
    override suspend fun runCode(projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("project_id", projectId)
        }

        currentSocket.emit(RUN_CODE, payload)
    }

    /**
     * Отправляет команду на сервер через веб-сокет для принудительной остановки выполнения кода текущего проекта.
     *
     * @param projectId Идентификатор останавливаемого проекта.
     */
    override suspend fun stopCode(projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("project_id", projectId)
        }

        currentSocket.emit(STOP_CODE, payload)
    }

    /**
     * Устанавливает подписку на поток вывода консоли (stdout/stderr) для запущенного проекта.
     *
     * @param projectId Идентификатор отслеживаемого проекта.
     * @return [Flow] со строками вывода консоли приложения.
     */
    override fun observeConsoleOutput(projectId: Int): Flow<String> = callbackFlow {
        val currentSocket = getConnectedSocket()
        if (currentSocket == null) {
            close(Exception("Token is null or socket failed"))
            return@callbackFlow
        }

        currentSocket.on(CONSOLE_OUTPUT) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject

                if (data == null) {
                    return@on
                }

                val output = data.optString("data", null) ?: return@on

                trySend(output)
            } catch (_: Exception) {
            }
        }

        awaitClose {
            currentSocket.off(CONSOLE_OUTPUT)
        }
    }

    /**
     * Закрывает соединение с комнатой проекта, отписывается от всех событий и отключает веб-сокет.
     */
    override suspend fun leaveFromProjectRoom(projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("project_id", projectId)
        }

        currentSocket.emit(LEAVE_PROJECT_ROOM, payload)
        closeSocket()
    }

    override fun closeSocket() {
        socket?.disconnect()
        socket?.off()
        socket = null
    }

    override suspend fun sendInput(input: String, projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("input", input)
            put("project_id", projectId)
        }

        currentSocket.emit(SEND_INPUT, payload)
    }

    override suspend fun joinChatRoom(identificator: String, projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("identificator", identificator)
            put("project_id", projectId)
        }

        currentSocket.emit(JOIN_CHAT_ROOM, payload)
    }

    override suspend fun leaveChatRoom(identificator: String, projectId: Int) {
        val currentSocket = getConnectedSocket() ?: return

        val payload = JSONObject().apply {
            put("identificator", identificator)
            put("project_id", projectId)
        }

        currentSocket.emit(LEAVE_CHAT_ROOM, payload)
    }

    override fun observeMessages(): Flow<Pair<Int, List<Message>>> = callbackFlow {
        val currentSocket = getConnectedSocket()
        if (currentSocket == null) {
            close(Exception("Token is null or socket failed"))
            return@callbackFlow
        }

        currentSocket.on(GET_MESSAGES) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject
                if (data == null) return@on

                val chatId = data.optInt("chat_id", 0)
                if (chatId == 0) return@on

                val messagesArray = data.optJSONArray("messages") ?: return@on

                val messages = mutableListOf<Message>()

                for (i in 0 until messagesArray.length()) {
                    val item = messagesArray.getJSONObject(i)

                    val id = item.optInt("id", 0)
                    val text = item.optString("text", null)
                    val author = item.optString("author", null)
                    val sendTime = item.optString("send_time", null)

                    if (id == 0 || text == null || author == null || sendTime == null) {
                        continue
                    }

                    messages.add(
                        Message(
                            id = id,
                            text = text,
                            author = author,
                            sendTime = sendTime
                        )
                    )
                }

                trySend(Pair(chatId, messages))
            } catch (_: Exception) {
            }
        }

        awaitClose {
            currentSocket.off(GET_MESSAGES)
        }
    }

    override fun observeRemovedFromProject(): Flow<Int> = callbackFlow {
        val currentSocket = getConnectedSocket()
        if (currentSocket == null) {
            close(Exception("Token is null or socket failed"))
            return@callbackFlow
        }

        currentSocket.on(REMOVED_FROM_PROJECT) { args ->
            try {
                val data = args.firstOrNull { it is JSONObject } as? JSONObject
                if (data == null) return@on

                val projectId = data.optInt("project_id", 0)
                if (projectId == 0) return@on

                trySend(projectId)
            } catch (_: Exception) {
            }
        }

        awaitClose {
            currentSocket.off(REMOVED_FROM_PROJECT)
        }
    }
}