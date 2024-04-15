package com.tirexmurina.shiftlabtest2024sp.domain.usecase

import com.tirexmurina.shiftlabtest2024sp.domain.entity.User
import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository

class SaveUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) = repository.saveUser(user)
}