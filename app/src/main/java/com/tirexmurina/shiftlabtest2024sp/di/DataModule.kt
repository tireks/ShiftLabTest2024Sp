package com.tirexmurina.shiftlabtest2024sp.di

import com.tirexmurina.shiftlabtest2024sp.data.UserRepositoryImpl
import com.tirexmurina.shiftlabtest2024sp.data.converters.UserConverterFromLocal
import com.tirexmurina.shiftlabtest2024sp.data.converters.UserConverterToLocal
import com.tirexmurina.shiftlabtest2024sp.domain.repository.UserRepository
import com.tirexmurina.shiftlabtest2024sp.localstore.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUserConverterFromLocal() : UserConverterFromLocal = UserConverterFromLocal()

    @Provides
    @Singleton
    fun provideUserConverterToLocal() : UserConverterToLocal = UserConverterToLocal()

    @Provides
    @Singleton
    fun provideUserRepository(
        dao: UserDao,
        converterToLocal: UserConverterToLocal,
        converterFromLocal: UserConverterFromLocal
    ) : UserRepository {
        return UserRepositoryImpl(
            dao = dao,
            converterFromLocal = converterFromLocal,
            converterToLocal = converterToLocal
        )
    }

}