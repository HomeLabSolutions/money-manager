package com.d9tilov.moneymanager.category.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.data.CategoryRepoImpl
import com.d9tilov.moneymanager.category.data.local.CategoryLocalSource
import com.d9tilov.moneymanager.category.data.local.CategorySource
import com.d9tilov.moneymanager.category.data.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.category.domain.CategoryInteractorImpl
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.domain.CategoryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class CategoryModule {

    @Provides
    fun provideCategoryInteractor(categoryRepo: CategoryRepo): CategoryInteractor =
        CategoryInteractorImpl(categoryRepo)

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryLocalSource(
        preferencesStore: PreferencesStore,
        categoryMapper: CategoryMapper,
        prepopulateDataManager: PrepopulateDataManager,
        database: AppDatabase
    ): CategorySource = CategoryLocalSource(preferencesStore, categoryMapper, prepopulateDataManager, database)

    @Provides
    fun categoryRepo(categoryLocalSource: CategorySource): CategoryRepo =
        CategoryRepoImpl(
            categoryLocalSource
        )
}