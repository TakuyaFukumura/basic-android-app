package com.example.myapplication.di

import com.example.myapplication.data.dao.StringDao
import com.example.myapplication.data.dao.StringDaoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideStringDao(): StringDao {
        // 実際のStringDaoの生成処理に合わせて実装してください
        return StringDaoImpl()
    }
}
