package com.d9tilov.android.category.di

import com.d9tilov.android.category.data.contract.CategorySource
import com.d9tilov.android.category.data.contract.DefaultCategoriesManager
import com.d9tilov.android.category.data.impl.CategoryLocalSource
import com.d9tilov.android.category.data.impl.CategoryRepoImpl
import com.d9tilov.android.category.data.impl.DefaultCategoriesManagerImpl
import com.d9tilov.android.category.domain.contract.CategoryRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val categoryDataModule =
    module {
        factory<DefaultCategoriesManagerImpl>() bind DefaultCategoriesManager::class
        factory<CategoryLocalSource>() bind CategorySource::class
        factory<CategoryRepoImpl>() bind CategoryRepo::class
    }
