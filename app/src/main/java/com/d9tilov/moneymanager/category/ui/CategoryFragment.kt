package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
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
    private val sum by lazy { args.sum }

    override fun getNavigator() = this
    override fun getToolbarTitle() = getString(R.string.title_category)
    override val viewModel by viewModels<CategoryViewModel>()

    override fun openSubCategoryScreen(category: Category) {
        val action = CategoryFragmentDirections.toSubCategoryDest(
            destination = destination,
            parentCategory = category,
            transactionType = transactionType,
            sum = sum
        )
        findNavController().navigate(action)
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = CategoryFragmentDirections.toCategoryCreationDest(category, CategoryDestination.CATEGORY_SCREEN, transactionType)
        findNavController().navigate(action)
    }

    override fun openRemoveDialog(category: Category) {
        val action = CategoryFragmentDirections.toRemoveCategoryDialog(category, destination, transactionType)
        findNavController().navigate(action)
    }

    override fun backToEditTransactionScreen(category: Category) {
        val action = CategoryFragmentDirections.toEditTransactionDest(
            requireNotNull(transaction), category, transactionType, destination
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.categoryCreate?.setOnClickListener {
            val action = CategoryFragmentDirections.toCategoryCreationDest(transactionType = transactionType, destination = destination)
            findNavController().navigate(action)
        }
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
