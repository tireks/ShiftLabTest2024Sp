package com.tirexmurina.shiftlabtest2024sp.domain.usecase

import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository

class DeleteUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.deleteUser()
}