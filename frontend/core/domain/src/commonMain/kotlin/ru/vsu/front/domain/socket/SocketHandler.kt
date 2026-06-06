package ru.vsu.front.domain.socket

import io.socket.client.Socket

/**
 * Интерфейс класса, который должен держать в себе подключение к сокету.
 */
interface SocketHandler {
    /**
     * Возвращает подключенный сокет.
     */
    suspend fun getConnectedSocket(): Socket?
    /**
     * Закрывает сокет.
     */
    fun closeSocket()
}