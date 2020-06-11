package com.d9tilov.moneymanager.category.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.data.CategoryDataRepo
import com.d9tilov.moneymanager.category.data.local.CategoryLocalSource
import com.d9tilov.moneymanager.category.data.local.CategorySource
import com.d9tilov.moneymanager.category.data.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.category.domain.CategoryUserInteractor
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.domain.CategoryRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CategoryModule {

    @Provides
    fun provideCategoryInteractor(categoryRepo: CategoryRepo): CategoryInteractor =
        CategoryUserInteractor(categoryRepo)

    @Provides
    @Singleton
    fun provideCategoryLocalSource(
        preferencesStore: PreferencesStore,
        categoryMapper: CategoryMapper,
        prepopulateDataManager: PrepopulateDataManager,
        database: AppDatabase
    ): CategorySource = CategoryLocalSource(preferencesStore, categoryMapper, prepopulateDataManager, database)

    @Provides
    fun categoryRepo(categoryLocalSource: CategorySource): CategoryRepo =
        CategoryDataRepo(
            categoryLocalSource
        )
}
