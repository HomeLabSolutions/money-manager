package com.d9tilov.moneymanager.category.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.SimpleItemAnimator
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.CategoryNavigator
import com.d9tilov.moneymanager.category.common.BaseCategoryFragment
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination
import com.d9tilov.moneymanager.category.ui.recycler.SimpleItemTouchHelperCallback
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.android.core.events.OnItemMoveListener
import com.d9tilov.android.core.model.isIncome
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.LayoutEmptyListPlaceholderBinding
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment.Companion.ARG_TRANSACTION_CREATED
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment :
    BaseCategoryFragment<CategoryNavigator>(),
    CategoryNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<CategoryViewModel>()
    private var viewStub: LayoutEmptyListPlaceholderBinding? = null

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun openSubCategoryScreen(category: Category) {
        val action = CategoryFragmentDirections.toSubCategoryDest(
            destination,
            transactionType,
            category
        )
        findNavController().navigate(action)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.ITEM_ID, "open_subcategory_screen")
        }
    }

    override fun openCreateCategoryScreen(category: Category) {
        val action = CategoryFragmentDirections.toCategoryCreationDest(category)
        findNavController().navigate(action)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.ITEM_ID, "open_creation_category_screen")
        }
    }

    override fun openRemoveDialog(category: Category) {
        val action = CategoryFragmentDirections.toRemoveCategoryDialog(
            CategoryDestination.CategoryScreen,
            category
        )
        findNavController().navigate(action)
    }

    override fun backToEditTransactionScreen(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_CATEGORY, category)
        findNavController().popBackStack()
    }

    override fun backToEditRegularTransactionScreen(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_CATEGORY, category)
        findNavController().popBackStack()
    }

    override fun backToMainScreen(category: Category) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            ARG_TRANSACTION_CREATED,
            category
        )
        findNavController().popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemMoveListener = onItemMoveListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStub = viewBinding?.categoryEmptyPlaceholder
        toolbar?.title = getString(R.string.title_category)
        viewBinding?.categoryCreate?.setOnClickListener {
            val action = CategoryFragmentDirections.toCategoryCreationDest(
                if (transactionType.isIncome()) Category.EMPTY_INCOME
                else Category.EMPTY_EXPENSE
            )
            findNavController().navigate(action)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { list ->
                    if (list.isEmpty()) showViewStub()
                    else hideViewStub()
                }
        }
        val callback = SimpleItemTouchHelperCallback(categoryAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(viewBinding?.categoryRv)
        (viewBinding?.categoryRv?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
    }

    private fun showViewStub() {
        viewStub?.let {
            it.root.show()
            it.emptyPlaceholderIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_categories_empty
                )
            )
            it.emptyPlaceholderTitle.text =
                getString(R.string.category_empty_placeholder_title)
            it.emptyPlaceholderSubtitle.show()
            it.emptyPlaceholderSubtitle.text =
                getString(R.string.category_empty_placeholder_subtitle)
        }
    }

    private fun hideViewStub() {
        viewStub?.root?.gone()
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
