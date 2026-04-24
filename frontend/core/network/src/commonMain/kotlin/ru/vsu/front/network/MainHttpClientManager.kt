package ru.vsu.front.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import ru.vsu.front.auth.AuthManager
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.usecase.RefreshUseCase

/**
 * Создает и настраивает экземпляр [HttpClient] для выполнения запросов, в заголовке которых может быть токен.
 * * Использует движок [CIO].
 * * @param baseUrl базовый url для запросов.
 *
 * @return Готовый HTTP-клиент.
 */
class MainHttpClientManager(
    private val baseUrl: String,
    private val tokenStorage: TokenStorage,
    private val refreshUseCase: RefreshUseCase,
    private val authManager: AuthManager
) {
    private var _httpClient: HttpClient? = null

    /**
     * Возвращает экземпляр [HttpClient].
     *
     * Если не создан, создаёт и после возвращает.
     */
    fun getClient(): HttpClient {
        return _httpClient ?: createClient().also { _httpClient = it }
    }


    /**
     * Очищает [HttpClient], необходимо для обновления токенов при выходе из аккаунта.
     */
    fun invalidateClient() {
        _httpClient?.close()
        _httpClient = null
    }


    /**
     * Создаёт экземпляр [withJWTTokensHttpClient].
     *
     * @return Готовый [HttpClient].
     */
    private fun createClient(): HttpClient {
        return withJWTTokensHttpClient(
            baseUrl = baseUrl,
            tokenStorage = tokenStorage,
            refreshUseCase = refreshUseCase,
            authManager = authManager
        )
    }
}