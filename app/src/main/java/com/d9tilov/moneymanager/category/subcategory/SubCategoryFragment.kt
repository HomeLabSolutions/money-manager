package com.d9tilov.moneymanager.category.subcategory

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.EditTransactionFragment.Companion.ARG_CATEGORY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator>(),
    SubCategoryNavigator {

    private val args by navArgs<SubCategoryFragmentArgs>()
    private val parentCategory by lazy { args.parentCategory }
    private val transactionType by lazy { args.transactionType }

    override fun getNavigator() = this
    override fun getToolbarTitle(): String =
        getString(R.string.title_sub_category, parentCategory.name)

    override val viewModel by viewModels<SubCategoryViewModel>()

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().getBackStackEntry(R.id.edit_transaction_dest).savedStateHandle.set(
            ARG_CATEGORY,
            category
        )
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun backToMainScreen(transactionType: TransactionType) {
        findNavController().popBackStack(R.id.category_dest, true)
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = SubCategoryFragmentDirections.toCategoryCreationDest(
            transactionType,
            category
        )
        findNavController().navigate(action)
    }

    override fun openRemoveDialog(subCategory: Category) {
        val action = SubCategoryFragmentDirections.toRemoveSubCategoryDialog(subCategory)
        findNavController().navigate(action)
    }
}
