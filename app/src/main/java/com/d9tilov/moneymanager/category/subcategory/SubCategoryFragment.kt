package com.d9tilov.moneymanager.category.subcategory

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.SubCategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.subcategory.vm.SubCategoryViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoryFragment :
    BaseCategoryFragment<SubCategoryNavigator>(),
    SubCategoryNavigator {

    private val args by navArgs<SubCategoryFragmentArgs>()
    private val parentCategory by lazy { args.parentCategory }
    private val transaction by lazy { args.editedTransaction }
    private val destination by lazy { args.destination }
    private val transactionType by lazy { args.transactionType }

    override fun getNavigator() = this
    override fun getToolbarTitle(): String =
        getString(R.string.title_sub_category, parentCategory.name)

    override val viewModel by viewModels<SubCategoryViewModel>()

    override fun backToEditTransactionScreen(category: Category) {
        val action = SubCategoryFragmentDirections.toEditTransactionDest(
            requireNotNull(transaction), category, transactionType
        )
        findNavController().navigate(action)
    }

    override fun backToMainScreen(transactionType: TransactionType) {
        val action = SubCategoryFragmentDirections.toIncomeExpenseDest(transactionType)
        findNavController().navigate(action)
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = SubCategoryFragmentDirections.toCategoryCreationDest(category, CategoryDestination.SUB_CATEGORY_SCREEN, transactionType)
        findNavController().navigate(action)
    }
}
