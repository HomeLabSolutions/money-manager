package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
import androidx.core.content.ContextCompat
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
import com.d9tilov.moneymanager.core.constants.DataConstants
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DECIMAL_LENGTH
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
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
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_HALF_UP
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment :
    BaseIncomeExpenseFragment<ExpenseNavigator>(R.layout.fragment_expense),
    ExpenseNavigator,
    OnKeyboardVisibleChange {

    private val viewBinding by viewBinding(FragmentExpenseBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<ExpenseViewModel>()
    override val snackBarAnchorView by lazy { categoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun initViews() {
        emptyViewStub = viewBinding.root.findViewById(R.id.expense_transaction_empty_placeholder)
        mainSum = viewBinding.expenseMainSum
        mainSumTitle = viewBinding.expenseMainSumTitle
        categoryRvList = viewBinding.expenseCategoryRvList
        transactionRvList = viewBinding.expenseTransactionRvList
        infoLayout = viewBinding.expenseInfoLayout
        transactionBtnAdd = viewBinding.expenseTransactionBtnAdd
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        lifecycleScope.launchWhenStarted {
            viewModel.transactions.collectLatest { data ->
                transactionAdapter.submitData(data)
                viewBinding.expenseTransactionRvList.scrollToPosition(0)
            }
        }
        lifecycleScope.launchWhenStarted {
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
        viewModel.spentInPeriod.observe(
            viewLifecycleOwner,
            { sum ->
                if (sum.signum() == 0) {
                    viewBinding.expensePeriodInfoUsdValue.gone()
                    viewBinding.expensePeriodInfoApproxSign.gone()
                } else {
                    viewBinding.expensePeriodInfoUsdValue.show()
                    viewBinding.expensePeriodInfoApproxSign.show()
                    viewBinding.expensePeriodInfoUsdValue.setValue(
                        sum,
                        DataConstants.DEFAULT_CURRENCY_CODE
                    )
                }
            }
        )
        viewModel.spentInPeriodApprox.observe(
            viewLifecycleOwner,
            viewBinding.expensePeriodInfoApproxSum::setValue
        )
        viewModel.ableToSpendToday.observe(
            viewLifecycleOwner,
            { sum ->
                viewBinding.expenseTodayInfoValue.setValue(sum)
                viewBinding.expenseTodayInfoValue.setColor(
                    if (sum.setScale(DECIMAL_LENGTH, ROUND_HALF_UP).signum() > 0)
                        ContextCompat.getColor(requireContext(), R.color.success_color) else
                        ContextCompat.getColor(requireContext(), R.color.error_color)
                )
            }
        )
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
                    HORIZONTAL
                )
            )
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    override fun initTransactionsRecyclerView() {
        viewBinding.run {
            expenseTransactionRvList.layoutManager = LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration = StickyHeaderItemDecorator(transactionAdapter)
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : TransactionSwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
            // промаргивание после обновления элемента из-за функции crossFade
        }
    }

    override fun openCategoriesScreen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_expense")
        }
        viewBinding.run {
            val inputSum = mainSum.getValue()
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
        onOpenKeyboardBase()
    }

    override fun onCloseKeyboard() {
        onCloseKeyboardBase()
    }

    override fun saveTransaction(category: Category) {
        viewModel.saveTransaction(
            category,
            mainSum.getValue()
        )
        isTransactionDataEmpty = false
    }

    override fun resetMainSum() {
        mainSum.setValue(BigDecimal.ZERO)
    }

    override fun getType() = TransactionType.EXPENSE

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}
