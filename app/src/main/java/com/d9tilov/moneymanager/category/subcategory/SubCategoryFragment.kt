package com.d9tilov.moneymanager.category.subcategory

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator>(),
    SubCategoryNavigator {

    private val args by navArgs<SubCategoryFragmentArgs>()
    private val parentCategory by lazy { args.parentCategory }

    override fun getNavigator() = this
    override fun getToolbarTitle(): String =
        getString(R.string.title_sub_category, parentCategory.name)

    override val viewModel by viewModels<SubCategoryViewModel>()

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }
}
