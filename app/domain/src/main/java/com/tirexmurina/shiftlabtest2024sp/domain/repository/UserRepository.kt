package com.tirexmurina.shiftlabtest2024sp.domain.repository

import com.tirexmurina.shiftlabtest2024sp.domain.entity.User

interface UserRepository {

    suspend fun getUser() : User

    suspend fun saveUser(user: User)

    suspend fun deleteUser()

}