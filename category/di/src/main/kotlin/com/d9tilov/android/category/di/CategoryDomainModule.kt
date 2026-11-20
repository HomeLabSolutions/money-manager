package com.d9tilov.android.category.di

import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.category.domain.impl.CategoryInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface CategoryDomainModule {
    @Binds
    val CategoryInteractorImpl.provideCategoryInteractor: CategoryInteractor
}
