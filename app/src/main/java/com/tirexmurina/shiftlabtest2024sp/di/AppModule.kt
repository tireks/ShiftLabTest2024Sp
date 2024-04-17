package com.tirexmurina.shiftlabtest2024sp.di

import com.tirexmurina.shiftlabtest2024sp.utils.DateUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideDateUtil(): DateUtils {
        return DateUtils()
    }

}