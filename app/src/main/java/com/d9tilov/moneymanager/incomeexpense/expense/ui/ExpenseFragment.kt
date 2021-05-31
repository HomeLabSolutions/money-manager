package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.R
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
import com.d9tilov.moneymanager.regular.PeriodicTransactionWorker
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.callback.TransactionSwipeToDeleteCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    private val categoryGroup = mutableListOf<View>()
    private val transactionGroup = mutableListOf<View>()

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyViewStub = viewBinding.root.findViewById(R.id.expense_transaction_empty_placeholder)
        categoryGroup.run {
            add(viewBinding.expenseCategoryRvList)
            add(viewBinding.expenseInfoLayout)
            add(viewBinding.expenseMainSumTitle)
            add(viewBinding.expenseMainSum)
        }
        transactionGroup.run {
            add(viewBinding.expenseTransactionBtnAdd)
            add(viewBinding.expenseTransactionRvList)
        }
        categoryGroup.forEach { it.gone() }
        transactionGroup.forEach { it.gone() }
        mainSum = viewBinding.expenseMainSum
        viewBinding.run {
            expenseMainSum.moneyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    expenseMainSum.moneyEditText.post {
                        expenseMainSum.moneyEditText.setSelection(expenseMainSum.moneyEditText.text.toString().length)
                    }
                }
            }
        }
        viewModel.run {
            categories.observe(
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
            getTransactionEvent().observe(
                viewLifecycleOwner,
                {
                    hideKeyboard()
                    resetMainSum()
                }
            )
        }
        viewModel.regularTransactions.observe(
            viewLifecycleOwner,
            {
                for (transaction in it) {
                    PeriodicTransactionWorker.startPeriodicJob(requireContext(), transaction)
                }
            }
        )
        lifecycleScope.launch {
            viewModel.transactions.collectLatest {
                transactionAdapter.submitData(
                    it
                )
            }
        }
        lifecycleScope.launch {
            transactionAdapter
                .loadStateFlow
                .collectLatest { loadStates ->
                    isTransactionDataEmpty =
                        loadStates.source.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached && transactionAdapter.itemCount == 0
                    if (isTransactionDataEmpty && !(activity as MainActivity).forceShowKeyboard) {
                        showViewStub(TransactionType.EXPENSE)
                    } else {
                        hideViewStub()
                    }
                }
        }
        lifecycleScope.launch {
            viewModel.spentInPeriod.observe(
                viewLifecycleOwner,
                {
                    viewBinding.expensePeriodInfoValue.setValue(it)
                }
            )
        }
        viewBinding.expenseTransactionRvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val fab = viewBinding.expenseTransactionBtnAdd
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab.isShown) {
                    fab.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        viewBinding.expenseTransactionBtnAdd.setOnClickListener {
            viewBinding.expenseMainSum.moneyEditText.requestFocus()
            showKeyboard(viewBinding.expenseMainSum.moneyEditText)
        }
    }

    override fun onStart() {
        super.onStart()
        if ((activity as MainActivity).forceShowKeyboard) {
            showKeyboard(viewBinding.expenseMainSum.moneyEditText)
        } else {
            crossfade(false)
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
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    override fun initTransactionsRecyclerView() {
        viewBinding.run {
            expenseTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration = StickyHeaderItemDecorator(transactionAdapter)
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : TransactionSwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_expense")
        }
        viewBinding.run {
            val inputSum = viewBinding.expenseMainSum.getValue()
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
        isKeyboardOpen = true
        viewBinding.run {
            crossfade(true)
            hideViewStub()
            viewBinding.expenseCategoryRvList.scrollToPosition(0)
        }
    }

    override fun onCloseKeyboard() {
        isKeyboardOpen = false
        viewBinding.run {
            crossfade(false)
            if (isTransactionDataEmpty) {
                showViewStub(TransactionType.EXPENSE)
            }
            viewBinding.expenseMainSum.clearFocus()
        }
    }

    private fun crossfade(openKeyboard: Boolean) {
        val hiddenGroup = if (openKeyboard) transactionGroup else categoryGroup
        val shownGroup = if (openKeyboard) categoryGroup else transactionGroup
        shownGroup.forEach {
            it.apply {
                alpha = 0f
                show()
                animate()
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(null)
            }
        }
        hiddenGroup.forEach { it.gone() }
    }

    override fun saveTransaction(category: Category) {
        viewModel.saveTransaction(
            category,
            viewBinding.expenseMainSum.getValue()
        )
        isTransactionDataEmpty = false
    }

    override fun resetMainSum() {
        viewBinding.expenseMainSum.setValue(BigDecimal.ZERO)
    }

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}
