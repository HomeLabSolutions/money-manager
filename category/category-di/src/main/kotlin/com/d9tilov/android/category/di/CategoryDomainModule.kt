package com.d9tilov.android.category.di

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.contract.CategoryRepo
import com.d9tilov.android.category.domain.impl.CategoryInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object CategoryDomainModule {

    @Provides
    @ActivityRetainedScoped
    fun provideCategoryInteractor(categoryRepo: CategoryRepo): CategoryInteractor =
        CategoryInteractorImpl(categoryRepo)
}
