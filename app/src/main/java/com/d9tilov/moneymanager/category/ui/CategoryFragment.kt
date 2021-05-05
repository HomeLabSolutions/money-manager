package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.ui.recycler.SimpleItemTouchHelperCallback
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.moneymanager.core.events.OnItemMoveListener
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment.Companion.ARG_TRANSACTION_CREATED
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment :
    BaseCategoryFragment<CategoryNavigator>(),
    CategoryNavigator {

    private val args by navArgs<CategoryFragmentArgs>()
    private val transactionType by lazy { args.transactionType }
    private val sum by lazy { args.sum }

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryViewModel>()

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun openSubCategoryScreen(category: Category) {
        val action = CategoryFragmentDirections.toSubCategoryDest(
            destination,
            transactionType,
            category,
            sum
        )
        findNavController().navigate(action)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.ITEM_ID, "open_subcategory_screen")
        }
    }

    override fun openCreateCategoryScreen(category: Category?) {
        val action = CategoryFragmentDirections.toCategoryCreationDest(
            transactionType,
            category
        )
        findNavController().navigate(action)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.ITEM_ID, "open_creation_category_screen")
        }
    }

    override fun openRemoveDialog(category: Category) {
        val action = CategoryFragmentDirections.toRemoveCategoryDialog(
            CategoryDestination.CATEGORY_SCREEN,
            category
        )
        findNavController().navigate(action)
    }

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_CATEGORY, category)
        findNavController().popBackStack()
    }

    override fun backToPeriodicTransactionCreationScreen(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_CATEGORY, category)
        findNavController().popBackStack()
    }

    override fun backToMainScreen(transactionType: TransactionType) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            ARG_TRANSACTION_CREATED,
            true
        )
        findNavController().popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemMoveListener = onItemMoveListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.title = getString(R.string.title_category)
        viewBinding.categoryCreate.setOnClickListener {
            val action = CategoryFragmentDirections.toCategoryCreationDest(transactionType)
            findNavController().navigate(action)
        }
        if (destination == CategoryDestination.MAIN_SCREEN || destination == CategoryDestination.PREPOPULATE_SCREEN) {
            val callback =
                SimpleItemTouchHelperCallback(
                    categoryAdapter
                )
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(viewBinding.categoryRv)
        }
        (viewBinding.categoryRv.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
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
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(
                    "category_add_to_folder",
                    "folder: " + parentItem.name + " child: " + childItem.name
                )
            }
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
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                param(
                    "category_unit_to_folder",
                    "firstItem: " + firstItem.name + " secondItem: " + secondItem.name
                )
            }
        }
    }

    companion object {
        fun create() = CategoryFragment()
    }
}
