package com.d9tilov.moneymanager.category.di.provider

import com.d9tilov.moneymanager.category.di.CategoryFragmentModule
import com.d9tilov.moneymanager.category.di.SubCategoryFragmentModule
import com.d9tilov.moneymanager.category.subcategory.SubCategoryFragment
import com.d9tilov.moneymanager.category.ui.CategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CategoryFrgProvider {

    @ContributesAndroidInjector(modules = [CategoryFragmentModule::class])
    abstract fun provideCategoryFragment(): CategoryFragment

    @ContributesAndroidInjector(modules = [SubCategoryFragmentModule::class])
    abstract fun provideSubCategoryFragment(): SubCategoryFragment
}
