package com.d9tilov.moneymanager.category.subcategory

import android.content.Context
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel

class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator, SubCategoryViewModel>(),
    SubCategoryNavigator {

    private lateinit var parentCategory: Category

    override fun getViewModelClass(): Class<SubCategoryViewModel> = SubCategoryViewModel::class.java
    override fun getNavigator() = this
    override fun getToolbarTitle(): String = getString(R.string.title_sub_category, parentCategory.name)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Category>(ARG_SUB_CATEGORY)?.let {
            parentCategory = it
        }
    }

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }

    companion object {
        const val ARG_SUB_CATEGORY = "args_subcategory"
    }
}
