package com.d9tilov.moneymanager.category.subcategory

import android.content.Context
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator>(),
    SubCategoryNavigator {

    private lateinit var parentCategory: Category
    override fun getNavigator() = this
    override fun getToolbarTitle(): String = getString(R.string.title_sub_category, parentCategory.name)
    override val viewModel by viewModels<SubCategoryViewModel>()

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
