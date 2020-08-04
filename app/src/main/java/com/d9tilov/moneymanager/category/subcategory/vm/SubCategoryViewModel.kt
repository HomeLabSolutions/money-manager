package com.d9tilov.moneymanager.category.subcategory.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.CategoryInteractor

class SubCategoryViewModel @ViewModelInject constructor(
    private val categoryInteractor: CategoryInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    init {
        val parentCategory = savedStateHandle.get<Category>("parent_category")
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
        expenseCategories.value = parentCategory.children
    }

    override fun onCategoryClicked(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onCategoryRemoved(category: Category) {
        TODO("Not yet implemented")
    }

    override fun update(name: String) {
        TODO("Not yet implemented")
    }
}
