package com.d9tilov.android.category.di

import android.content.Context
import com.d9tilov.android.category.data.contract.CategoryRepo
import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.category.data.impl.CategoryLocalSource
import com.d9tilov.android.category.data.impl.CategoryRepoImpl
import com.d9tilov.android.category.data.impl.DefaultCategoriesManager
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object CategoryDataModule {

    @Provides
    @ActivityRetainedScoped
    fun provideDefaultCategoriesManager(context: Context): DefaultCategoriesManager =
        DefaultCategoriesManager(context)

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryLocalSource(
        preferencesStore: PreferencesStore,
        defaultCategoriesManager: DefaultCategoriesManager,
        database: AppDatabase
    ): CategorySource =
        CategoryLocalSource(
            preferencesStore,
            defaultCategoriesManager,
            database.categoryDao()
        )

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryRepo(categoryLocalSource: CategorySource): CategoryRepo =
        CategoryRepoImpl(categoryLocalSource)
}
