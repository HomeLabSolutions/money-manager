package com.d9tilov.android.category.di

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.category.data.contract.DefaultCategoriesManager
import com.d9tilov.android.category.data.impl.CategoryLocalSource
import com.d9tilov.android.category.data.impl.CategoryRepoImpl
import com.d9tilov.android.category.data.impl.DefaultCategoriesManagerImpl
import com.d9tilov.android.category.domain.contract.CategoryRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface CategoryDataModule {
    @Binds
    val DefaultCategoriesManagerImpl.provideDefaultCategoriesManager: DefaultCategoriesManager

    @Binds
    val CategoryLocalSource.provideCategoryLocalSource: CategorySource

    @Binds
    val CategoryRepoImpl.provideCategoryRepo: CategoryRepo
}
