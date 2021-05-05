package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.home.ui.MainActivity
import com.d9tilov.moneymanager.incomeexpense.ui.BaseIncomeExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.moneymanager.transaction.TransactionType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment :
    BaseIncomeExpenseFragment<ExpenseNavigator>(R.layout.fragment_expense),
    ExpenseNavigator,
    OnKeyboardVisibleChange {

    private val viewBinding by viewBinding(FragmentExpenseBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<ExpenseViewModel>()
    override val snackBarAnchorView by lazy { viewBinding.expenseCategoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            emptyViewStub = root.findViewById(R.id.expense_transaction_empty_placeholder)
            expenseMainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    expenseMainSum.moneyEditText.post {
                        expenseMainSum.moneyEditText.setSelection(expenseMainSum.moneyEditText.text.toString().length)
                    }
                }
            }
            mainSum = expenseMainSum
        }
        viewModel.run {
            getCategories().observe(
                viewLifecycleOwner,
                { list ->
                    val sortedCategories = list.sortedWith(
                        compareBy(
                            { it.children.isEmpty() },
                            { -it.usageCount },
                            { it.name }
                        )
                    )
                    categoryAdapter.updateItems(sortedCategories)
                }
            )
            getTransactions().observe(
                viewLifecycleOwner,
                {
                    lifecycleScope.launchWhenCreated {
                        transactionAdapter.submitData(it)
                        transactionAdapter.loadStateFlow.collectLatest { loadStates ->
                            isTransactionDataEmpty =
                                loadStates.source.refresh is LoadState.NotLoading &&
                                    loadStates.append.endOfPaginationReached &&
                                    transactionAdapter.itemCount == 0
                            if (isTransactionDataEmpty && !(activity as MainActivity).forceShowKeyboard) {
                                showViewStub(TransactionType.EXPENSE)
                            } else {
                                hideViewStub()
                            }
                        }
                    }
                }
            )
            getTransactionEvent().observe(
                viewLifecycleOwner,
                {
                    hideKeyboard()
                    resetMainSum()
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            showKeyboard(viewBinding.expenseMainSum)
        } else {
            viewBinding.run {
                expenseTransactionRvList.show()
                expenseCategoryRvList.gone()
            }
        }
    }

    override fun initCategoryRecyclerView() {
        viewBinding.run {
            val layoutManager =
                GridLayoutManager(
                    requireContext(),
                    if (requireContext().isTablet()) TABLET_SPAN_COUNT else SPAN_COUNT,
                    GridLayoutManager.HORIZONTAL,
                    false
                )
            expenseCategoryRvList.layoutManager = layoutManager
            expenseCategoryRvList.adapter = categoryAdapter
            expenseCategoryRvList.addItemDecoration(
                GridSpaceItemDecoration(
                    SPAN_COUNT,
                    resources.getDimension(R.dimen.recycler_view_category_offset).toInt(),
                    layoutManager.orientation
                )
            )
            val snapHelper: SnapHelper =
                ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    override fun initTransactionsRecyclerView() {
        viewBinding.run {
            expenseTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration =
                StickyHeaderItemDecorator(
                    transactionAdapter
                )
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_expense")
        }
        viewBinding.run {
            val inputSum = expenseMainSum.getValue()
            val action = if (inputSum.signum() > 0) {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_WITH_SUM_SCREEN,
                    sum = inputSum,
                    transactionType = TransactionType.EXPENSE
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_SCREEN,
                    transactionType = TransactionType.EXPENSE
                )
            }
            findNavController().navigate(action)
        }
    }

    override fun onOpenKeyboard() {
        Timber.tag(App.TAG).d("Keyboard shown")
        isKeyboardOpen = true
        viewBinding.run {
            onKeyboardVisibilityAnimation(true)
            expenseTransactionRvList.gone()
            hideViewStub()
            expenseCategoryRvList.scrollToPosition(0)
        }
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("Keyboard hidden")
        isKeyboardOpen = false
        viewBinding.run {
            onKeyboardVisibilityAnimation(false)
            if (isTransactionDataEmpty) {
                showViewStub(TransactionType.EXPENSE)
            }
            expenseMainSum.clearFocus()
        }
    }

    private fun onKeyboardVisibilityAnimation(open: Boolean) {
        viewBinding.run {
            if (open) {
                expenseCategoryRvList.show()
            } else {
                expenseTransactionRvList.show()
                expenseCategoryRvList.gone()
            }
        }
        val alphaAnimationCategories =
            ObjectAnimator.ofFloat(
                viewBinding.expenseCategoryRvList,
                View.ALPHA,
                if (open) ALPHA_CAT_MIN else ALPHA_MAX,
                if (open) ALPHA_MAX else ALPHA_CAT_MIN
            ).apply {
                duration = ANIMATION_DURATION_CAT
                interpolator = AccelerateInterpolator()
            }
        alphaAnimationCategories.start()
    }

    override fun saveTransaction(category: Category) {
        viewModel.saveTransaction(category, viewBinding.expenseMainSum.getValue())
    }

    override fun resetMainSum() {
        viewBinding.expenseMainSum.setValue(BigDecimal.ZERO)
    }

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}
