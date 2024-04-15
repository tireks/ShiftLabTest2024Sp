package com.tirexmurina.shiftlabtest2024sp.data

import com.tirexmurina.shiftlabtest2024sp.data.converters.UserConverterFromLocal
import com.tirexmurina.shiftlabtest2024sp.data.converters.UserConverterToLocal
import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository
import com.tirexmurina.shiftlabtest2024sp.localstore.UserDao

class UserRepositoryImpl(
    private val dao: UserDao,
    private val converterFromLocal: UserConverterFromLocal,
    private val converterToLocal: UserConverterToLocal
) : UserRepository{

    override suspend fun getUser(): User =
        converterFromLocal.convert(dao.getUser())

    override suspend fun saveUser(user: User) {
        dao.insertUser(converterToLocal.convert(user))
    }

    override suspend fun deleteUser() {
        dao.clearDatabase()
    }
}