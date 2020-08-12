package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.recycler.SimpleItemTouchHelperCallback
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.moneymanager.core.events.OnItemMoveListener
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.EditTransactionFragment.Companion.ARG_CATEGORY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment :
    BaseCategoryFragment<CategoryNavigator>(),
    CategoryNavigator {

    private val args by navArgs<CategoryFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val destination by lazy { args.destination }
    private val sum by lazy { args.sum }

    override fun getNavigator() = this
    override fun getToolbarTitle() = getString(R.string.title_category)
    override val viewModel by viewModels<CategoryViewModel>()

    override fun openSubCategoryScreen(category: Category) {
        val action = CategoryFragmentDirections.toSubCategoryDest(
            destination,
            transactionType,
            category,
            sum
        )
        findNavController().navigate(action)
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = CategoryFragmentDirections.toCategoryCreationDest(
            transactionType,
            category
        )
        findNavController().navigate(action)
    }

    override fun openRemoveDialog(category: Category) {
        val action = CategoryFragmentDirections.toRemoveCategoryDialog(category)
        findNavController().navigate(action)
    }

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_CATEGORY, category)
        findNavController().popBackStack()
    }

    override fun backToMainScreen(transactionType: TransactionType) {
        findNavController().popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemMoveListener = onItemMoveListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.categoryCreate?.setOnClickListener {
            val action = CategoryFragmentDirections.toCategoryCreationDest(transactionType)
            findNavController().navigate(action)
        }
        if (destination == CategoryDestination.MAIN_SCREEN) {
            val callback =
                SimpleItemTouchHelperCallback(
                    categoryAdapter
                )
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(viewBinding?.categoryRv)
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
                transactionType,
                childItem,
                parentItem
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
                transactionType,
                firstItem,
                secondItem
            )
            findNavController().navigate(action)
            categoryAdapter.enableEditMode(false)
        }
    }

    companion object {
        fun create() = CategoryFragment()
    }
}
