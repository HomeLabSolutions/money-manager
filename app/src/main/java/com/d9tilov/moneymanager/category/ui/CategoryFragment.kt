package com.d9tilov.moneymanager.category.ui

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment :
    BaseCategoryFragment<CategoryNavigator>(),
    CategoryNavigator {

    private val args by navArgs<CategoryFragmentArgs>()
    private val transaction by lazy { args.editedTransaction }
    private val destination by lazy { args.destination }

    override fun getNavigator() = this
    override fun getToolbarTitle() = getString(R.string.title_category)
    override val viewModel by viewModels<CategoryViewModel>()

    override fun openSubCategoryScreen(category: Category) {
        val action =
            CategoryFragmentDirections.toSubCategoryDest(destination, category)
        findNavController().navigate(action)
    }

    override fun openCreateCategoryDialog() {
        TODO("Not yet implemented")
    }

    override fun backToEditTransactionScreen(category: Category) {
        val action = CategoryFragmentDirections.toEditTransactionDest(
            requireNotNull(transaction), category
        )
        findNavController().navigate(action)
    }

    companion object {
        fun create() = CategoryFragment()
    }
}
