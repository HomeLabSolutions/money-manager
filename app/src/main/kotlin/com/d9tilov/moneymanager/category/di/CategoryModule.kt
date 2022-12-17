package com.d9tilov.moneymanager.category.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.category.data.CategoryRepoImpl
import com.d9tilov.moneymanager.category.data.local.CategoryLocalSource
import com.d9tilov.moneymanager.category.data.local.CategorySource
import com.d9tilov.moneymanager.category.data.local.mapper.CategoryMapper
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.domain.CategoryInteractorImpl
import com.d9tilov.moneymanager.category.domain.CategoryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object CategoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryInteractor(categoryRepo: CategoryRepo): CategoryInteractor =
        CategoryInteractorImpl(categoryRepo)

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryLocalSource(
        preferencesStore: PreferencesStore,
        categoryMapper: CategoryMapper,
        prepopulateDataManager: PrepopulateDataManager,
        database: AppDatabase
    ): CategorySource =
        CategoryLocalSource(
            preferencesStore,
            categoryMapper,
            prepopulateDataManager,
            database.categoryDao()
        )

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryRepo(categoryLocalSource: CategorySource): CategoryRepo =
        CategoryRepoImpl(categoryLocalSource)
}
