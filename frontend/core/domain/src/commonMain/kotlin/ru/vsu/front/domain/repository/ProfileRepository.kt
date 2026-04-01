package ru.vsu.front.domain.repository

import ru.vsu.front.model.entity.Response
import ru.vsu.front.model.entity.User

/**
 *
 *
 *
 */
interface ProfileRepository {

    /**
     *
     *
     *
     */
    suspend fun getProfile(userId: Int): Response<User>
}