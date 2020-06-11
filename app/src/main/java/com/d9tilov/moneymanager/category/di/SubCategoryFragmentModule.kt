package com.d9tilov.moneymanager.category.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.category.subcategory.SubCategoryFragment
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SubCategoryFragmentModule {

    @IntoMap
    @ViewModelKey(SubCategoryViewModel::class)
    @Provides
    fun provideSubCategoryViewModel(
        categoryInteractor: CategoryInteractor,
        fragment: SubCategoryFragment
    ): ViewModel {
        val parentCategory = fragment.requireArguments().getParcelable<Category>(SubCategoryFragment.ARG_SUB_CATEGORY)
        return SubCategoryViewModel(categoryInteractor, parentCategory)
    }
}
