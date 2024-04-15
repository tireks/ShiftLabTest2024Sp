package com.tirexmurina.shiftlabtest2024sp.data.converters

import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.localstore.model.UserModelLocal

// Класс-конвертер для преобразования объекта сущности User в локальную модель User.
class UserConverterToLocal {
    fun convert(from: User): UserModelLocal =
        with(from) {
            UserModelLocal(
                firstName = firstName,
                lastName = lastName
            )
        }
}