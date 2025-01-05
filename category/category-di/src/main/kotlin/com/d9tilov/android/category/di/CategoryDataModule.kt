package com.d9tilov.android.category.di

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.category.data.contract.DefaultCategoriesManager
import com.d9tilov.android.category.data.impl.CategoryLocalSource
import com.d9tilov.android.category.data.impl.CategoryRepoImpl
import com.d9tilov.android.category.data.impl.DefaultCategoriesManagerImpl
import com.d9tilov.android.category.domain.contract.CategoryRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
interface CategoryDataModule {
    @Binds
    @ActivityRetainedScoped
    fun provideDefaultCategoriesManager(defaultCategoriesManager: DefaultCategoriesManagerImpl): DefaultCategoriesManager

    @Binds
    @ActivityRetainedScoped
    fun provideCategoryLocalSource(categoryLocalSource: CategoryLocalSource): CategorySource

    @Binds
    @ActivityRetainedScoped
    fun provideCategoryRepo(categoryRepoImpl: CategoryRepoImpl): CategoryRepo
}
