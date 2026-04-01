package ru.vsu.front.data.repository

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.vsu.front.common.dispatcher_provider.DispatcherProvider
import ru.vsu.front.datastore.TokenStorage
import ru.vsu.front.domain.repository.ProfileRepository
import ru.vsu.front.model.entity.Project
import ru.vsu.front.model.entity.RequestError
import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User
import ru.vsu.front.network.HttpRoutes.PROFILE

class DefaultProfileRepository(
    private val httpClient: HttpClient,
    private val tokenStorage: TokenStorage
) : ProfileRepository {
    override suspend fun getProfile(userId: Int): Response<User> {
        return Response.Success(
            User(
                name = "gruzovik",
                email = "predal.kro@mail.ru",
                photoPath = "https://i.pravatar.cc/150?u=9",
                projects = buildList {
                    repeat(25) {
                        add(
                            Project(
                                id = it,
                                name = "Project $it",
                                ownerId = 1,
                                programmingLanguageId = 1
                            )
                        )
                    }
                }
            )
        )
    }
}