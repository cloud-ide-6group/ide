package ru.vsu.front.data

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.Polling
import ru.vsu.front.datastore.token_storage.TokenStorage
import ru.vsu.front.domain.socket.SocketHandler

class DefaultSocketHandler(
    private val tokenStorage: TokenStorage,
    private val baseUrl: String,
): SocketHandler {
    private var socket: Socket? = null
    private var activeToken: String? = null

    /**
     * Метод для получения текущего соединения.
     *
     * @return [Socket] или null, если токен отсутствует.
     */
    override suspend fun getConnectedSocket(): Socket? {
        val tokens = tokenStorage.getTokensAsync()

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


    /**
     * Отключает текущий сокет.
     */
    override fun closeSocket() {
        socket?.disconnect()
        socket?.off()
        socket = null
    }
}