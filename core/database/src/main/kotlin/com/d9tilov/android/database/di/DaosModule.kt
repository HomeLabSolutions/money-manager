package com.d9tilov.android.database.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.dao.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesCategoryDao(database: AppDatabase): CategoryDao = database.categoryDao()
}
