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

@Module
@InstallIn(ActivityRetainedComponent::class)
interface CategoryDataModule {
    @Binds
    fun provideDefaultCategoriesManager(impl: DefaultCategoriesManagerImpl): DefaultCategoriesManager

    @Binds
    fun provideCategoryLocalSource(impl: CategoryLocalSource): CategorySource

    @Binds
    fun provideCategoryRepo(impl: CategoryRepoImpl): CategoryRepo
}
