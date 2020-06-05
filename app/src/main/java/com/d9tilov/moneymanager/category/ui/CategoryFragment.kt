package com.d9tilov.moneymanager.category.ui

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.SubCategoryFragment.Companion.ARG_SUB_CATEGORY
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel

class CategoryFragment :
    BaseCategoryFragment<CategoryNavigator, CategoryViewModel>(),
    CategoryNavigator {

    override fun getViewModelClass() = CategoryViewModel::class.java
    override fun getNavigator() = this
    override fun getToolbarTitle() = getString(R.string.title_category)

    override fun openSubCategoryScreen(category: Category) {
        val bundle = bundleOf(ARG_SUB_CATEGORY to category)
        findNavController().navigate(R.id.action_mainFragment_to_sub_category_dest, bundle)
    }

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }

    companion object {
        fun create() = CategoryFragment()
    }
}
