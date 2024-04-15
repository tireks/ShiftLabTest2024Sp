package com.tirexmurina.shiftlabtest2024sp.di

import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.DeleteUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.GetUserUseCase
import com.tirexmurina.shiftlabtest2024sp.domain.usecase.SaveUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetUserUseCase(userRepository: UserRepository) : GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Provides
    fun provideSaveUserUseCase(userRepository: UserRepository) : SaveUserUseCase {
        return SaveUserUseCase(userRepository)
    }

    @Provides
    fun provideDeleteUserUseCase(userRepository: UserRepository) : DeleteUserUseCase {
        return DeleteUserUseCase(userRepository)
    }

}