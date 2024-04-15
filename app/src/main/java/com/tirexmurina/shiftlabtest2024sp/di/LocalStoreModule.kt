package com.tirexmurina.shiftlabtest2024sp.di

import android.content.Context
import com.tirexmurina.shiftlabtest2024sp.localstore.AppDatabase
import com.tirexmurina.shiftlabtest2024sp.localstore.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalStoreModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext app: Context): AppDatabase {
        return AppDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) : UserDao {
        return database.userDao()
    }

}