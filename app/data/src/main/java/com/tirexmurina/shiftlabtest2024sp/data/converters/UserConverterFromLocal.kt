package com.tirexmurina.shiftlabtest2024sp.data.converters

import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.localstore.model.UserModelLocal

// Класс-конвертер для преобразования локальной модели User в объект сущности User.
class UserConverterFromLocal {
    fun convert(from: UserModelLocal): User =
        with(from) {
            User(
                firstName = firstName,
                lastName = lastName
            )
        }
}