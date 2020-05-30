package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.BaseViewModel
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor

class SubCategoryViewModel(
    private val categoryInteractor: ICategoryInteractor,
    private val parentCategory: Category?
) : BaseViewModel<SubCategoryNavigator>() {

    val categories = MutableLiveData<List<Category>>()

    init {
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
        categories.value = parentCategory.children
    }
}
