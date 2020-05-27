package com.d9tilov.moneymanager.category.di

import com.d9tilov.moneymanager.category.ui.CategoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CategoryFrgProvider {

    @ContributesAndroidInjector
    abstract fun provideCategoryFragmentFactory(): CategoryFragment
}
