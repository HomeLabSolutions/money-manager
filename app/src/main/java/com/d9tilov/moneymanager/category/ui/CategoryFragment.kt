package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.moneymanager.core.events.OnItemMoveListener
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment :
    BaseCategoryFragment<CategoryNavigator>(),
    CategoryNavigator {

    private val args by navArgs<CategoryFragmentArgs>()
    private val transaction by lazy { args.editedTransaction }
    private val destination by lazy { args.destination }
    private val transactionType by lazy { args.transactionType }

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
            requireNotNull(transaction), category, transactionType
        )
        findNavController().navigate(action)
    }

    override fun backToMainScreen(transactionType: TransactionType) {
        val action = CategoryFragmentDirections.toIncomeExpenseDest(transactionType)
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemMoveListener = onItemMoveListener
    }

    private val onItemMoveListener = object : OnItemMoveListener<Category> {
        override fun onItemAddToFolder(
            childItem: Category,
            childPosition: Int,
            parentItem: Category,
            parentPosition: Int
        ) {
            val action = CategoryFragmentDirections.toCreateGroupDialog(
                childItem,
                parentItem,
                transactionType
            )
            findNavController().navigate(action)
            categoryAdapter.enableEditMode(false)
        }

        override fun onItemsUnitToFolder(
            firstItem: Category,
            firstChildPosition: Int,
            secondItem: Category,
            secondItemPosition: Int
        ) {
            val action = CategoryFragmentDirections.toCreateGroupDialog(
                firstItem,
                secondItem,
                transactionType
            )
            findNavController().navigate(action)
            categoryAdapter.enableEditMode(false)
        }
    }

    companion object {
        fun create() = CategoryFragment()
    }
}
