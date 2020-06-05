package com.d9tilov.moneymanager.category.subcategory.vm

import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryViewModel
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor

class SubCategoryViewModel(
    private val categoryInteractor: ICategoryInteractor,
    private val parentCategory: Category?
) : BaseCategoryViewModel<SubCategoryNavigator>() {

    init {
        if (parentCategory == null || parentCategory.children.isEmpty()) {
            throw IllegalArgumentException("Parent category mustn't have at least one child")
        }
        categories.value = parentCategory.children
    }

    override fun onCategoryClicked(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onCategoryRemoved(category: Category) {
        TODO("Not yet implemented")
    }

    override fun onItemSwap(categories: List<Category>) {
        TODO("Not yet implemented")
    }

    override fun update(name: String) {
        TODO("Not yet implemented")
    }
}
