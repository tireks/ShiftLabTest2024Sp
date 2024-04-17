package com.tirexmurina.shiftlabtest2024sp.data

import com.tirexmurina.shiftlabtest2024sp.data.converters.UserConverterFromLocal
import com.tirexmurina.shiftlabtest2024sp.data.converters.UserConverterToLocal
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.EmptyTableException
import com.tirexmurina.shiftlabtest2024sp.data.exceptions.SizeException
import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository
import com.tirexmurina.shiftlabtest2024sp.localstore.UserDao

class UserRepositoryImpl(
    private val dao: UserDao,
    private val converterFromLocal: UserConverterFromLocal,
    private val converterToLocal: UserConverterToLocal
) : UserRepository{

    override suspend fun getUser(): User {
        when(dao.getTableSize()){
            0 -> {
                throw EmptyTableException("Error! There is no content in database")
            }
            1 -> {
                return converterFromLocal.convert(dao.getUser())
            }
            else -> {
                dao.clearDatabase() // сразу подчистим базу
                throw SizeException("Error! Table size > 1. There may be garbage data left")
            }
        }
    }

    override suspend fun saveUser(user: User) {
        if (dao.getTableSize() > 0){
            dao.clearDatabase() // сразу подчистим базу,
            // тут тоже можно было бы пробросить SizeException,
            // но не вижу в этом большого смысла
        }
        dao.insertUser(converterToLocal.convert(user))
    }

    override suspend fun deleteUser() {
        dao.clearDatabase()
    }
}