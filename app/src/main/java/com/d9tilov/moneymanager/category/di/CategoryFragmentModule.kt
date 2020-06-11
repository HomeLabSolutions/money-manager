package com.d9tilov.moneymanager.category.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class CategoryFragmentModule {

    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    @Provides
    fun provideCategoryViewModel(
        categoryInteractor: CategoryInteractor
    ): ViewModel {
        return CategoryViewModel(categoryInteractor)
    }
}
