package com.example.myapplication.di

import com.example.myapplication.data.dao.StringDao
import com.example.myapplication.data.repository.StringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideStringRepository(stringDao: StringDao): StringRepository {
        return StringRepository(stringDao)
    }
}
