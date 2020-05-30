package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.category.subcategory.SubCategoryFragment
import javax.inject.Inject

class SubCategoryViewModelFactory @Inject constructor(
    private val categoryInteractor: ICategoryInteractor,
    private val subCategoryFragment: SubCategoryFragment
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass != SubCategoryViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        val parentCategory = subCategoryFragment.requireArguments()
            .getParcelable<Category>(SubCategoryFragment.ARG_SUB_CATEGORY)
        return SubCategoryViewModel(categoryInteractor) as T
    }
}
