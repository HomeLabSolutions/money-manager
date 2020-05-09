package com.d9tilov.moneymanager.data.di

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.data.category.CategoryRepoImpl
import com.d9tilov.moneymanager.data.category.local.CategoryLocalSource
import com.d9tilov.moneymanager.data.category.local.CategoryLocalSourceImpl
import com.d9tilov.moneymanager.data.category.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.domain.category.CategoryRepo
import dagger.Module
import dagger.Provides

@Module
class CategoryDataModule {

    @Provides
    fun provideCategoryLocalSource(
        categoryMapper: CategoryMapper,
        prepopulateDataManager: PrepopulateDataManager,
        database: AppDatabase
    ) : CategoryLocalSource = CategoryLocalSourceImpl(categoryMapper, prepopulateDataManager, database)

    @Provides
    fun categoryRepo(categoryLocalSource: CategoryLocalSource) :CategoryRepo = CategoryRepoImpl(categoryLocalSource)
}
