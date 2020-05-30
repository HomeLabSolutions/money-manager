package com.d9tilov.moneymanager.category.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.category.data.CategoryRepo
import com.d9tilov.moneymanager.category.data.local.CategoryLocalSource
import com.d9tilov.moneymanager.category.data.local.ICategoryLocalSource
import com.d9tilov.moneymanager.category.data.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.category.domain.ICategoryRepo
import dagger.Module
import dagger.Provides

@Module
class CategoryModule {

    @Provides
    fun provideCategoryInteractor(categoryRepo: ICategoryRepo): ICategoryInteractor =
        CategoryInteractor(categoryRepo)

    @Provides
    fun provideCategoryLocalSource(
        categoryMapper: CategoryMapper,
        prepopulateDataManager: PrepopulateDataManager,
        database: AppDatabase
    ): ICategoryLocalSource = CategoryLocalSource(categoryMapper, prepopulateDataManager, database)

    @Provides
    fun categoryRepo(categoryLocalSource: ICategoryLocalSource): ICategoryRepo =
        CategoryRepo(
            categoryLocalSource
        )
}
