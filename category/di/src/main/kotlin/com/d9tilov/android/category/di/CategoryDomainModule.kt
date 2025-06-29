package com.d9tilov.android.category.di

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.impl.CategoryInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface CategoryDomainModule {
    @Binds
    fun provideCategoryInteractor(impl: CategoryInteractorImpl): CategoryInteractor
}
