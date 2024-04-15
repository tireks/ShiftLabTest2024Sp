package com.tirexmurina.shiftlabtest2024sp.domain.usecase

import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository

class GetUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() : User = repository.getUser()

    //todo надо не забыть обработать различные типы ошибок,
    // например в импл намутить проверку что в таблице больше одной записи и если что,
    // пусть выкидывает спец эксепшн, в слое представления, соответственно, как-то специально реагировать
}