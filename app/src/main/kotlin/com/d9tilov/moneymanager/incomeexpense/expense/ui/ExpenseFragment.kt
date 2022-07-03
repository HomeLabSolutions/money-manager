package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.currencyCode
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.category.domain.entity.CategoryDestination
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.DECIMAL_LENGTH
import com.d9tilov.moneymanager.core.util.getColorFromAttr
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.incomeexpense.ui.BaseIncomeExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.moneymanager.transaction.domain.entity.ExpenseInfoUiModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionSpendingTodayModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.isIncome
import com.d9tilov.moneymanager.transaction.ui.callback.TransactionSwipeToDeleteCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal.ROUND_HALF_UP
import javax.inject.Inject

@AndroidEntryPoint
class ExpenseFragment :
    BaseIncomeExpenseFragment<ExpenseNavigator, FragmentExpenseBinding>(
        FragmentExpenseBinding::inflate,
        R.layout.fragment_expense
    ),
    ExpenseNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<ExpenseViewModel>()
    override val snackBarAnchorView by lazy { categoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun initViews() {
        viewBinding?.run {
            emptyViewStub = expenseTransactionEmptyPlaceholderInclude
            categoryRvList = expenseCategoryRvList
            transactionRvList = expenseTransactionLayoutInclude.expenseTransactionRvList
            transactionBtnAdd = expenseTransactionLayoutInclude.expenseTransactionBtnAdd
            infoLayout = expenseInfoLayoutInclude.root
            transactionsLayout = expenseTransactionLayoutInclude.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.categories.collect { list ->
                            val sortedCategories = list.sortedWith(
                                compareBy(
                                    { it.children.isEmpty() },
                                    { -it.usageCount },
                                    { it.name }
                                )
                            )
                            categoryAdapter.updateItems(sortedCategories)
                        }
                    }
                    launch { viewModel.transactions.collectLatest { transactionAdapter.submitData(it) } }
                    launch {
                        viewModel.expenseSpendingInfoPremium.collect { model: ExpenseInfoUiModel? ->
                            if (model != null) {
                                if (isKeyboardOpen()) expenseInfoLayoutInclude.root.show()
                                val spentInPeriod = model.spentInPeriodSum
                                if (spentInPeriod.signum() == 0) expenseInfoLayoutInclude.expensePeriodInfoApproxSign.gone()
                                else expenseInfoLayoutInclude.expensePeriodInfoApproxSign.show()

                                val spentInPeriodApprox = model.spentInPeriodSumApprox
                                expenseInfoLayoutInclude.expensePeriodInfoApproxSum.setValue(
                                    spentInPeriodApprox,
                                    currencyCode()
                                )
                                expenseInfoLayoutInclude.expensePeriodInfoApproxSum.show()

                                val spentToday = model.spentTodaySum
                                if (spentToday.signum() == 0) expenseInfoLayoutInclude.expenseTodayInfoApproxSign.gone()
                                else expenseInfoLayoutInclude.expenseTodayInfoApproxSign.show()

                                val spentTodayApprox = model.spentTodaySumApprox
                                expenseInfoLayoutInclude.expenseTodayInfoApproxSum.setValue(
                                    spentTodayApprox,
                                    currencyCode()
                                )
                                expenseInfoLayoutInclude.expenseTodayInfoApproxSum.show()

                                val ableToSpendToday = model.ableToSpendTodayModel
                                expenseInfoLayoutInclude.run {
                                    when (ableToSpendToday) {
                                        is TransactionSpendingTodayModel.OVERSPENDING -> {
                                            expenseCanSpendTodayInfoTitle.text =
                                                getString(R.string.category_expense_info_can_spend_today_negate_title)
                                            expenseTodayInfoValue.setValue(
                                                ableToSpendToday.trSum,
                                                currencyCode()
                                            )
                                            val infoColor = requireContext().getColorFromAttr(R.attr.colorError)
                                            expenseTodayInfoValue.setColor(infoColor)
                                        }
                                        is TransactionSpendingTodayModel.NORMAL -> {
                                            expenseCanSpendTodayInfoTitle.text =
                                                getString(R.string.category_expense_info_can_spend_today_title)
                                            expenseTodayInfoValue.run {
                                                setValue(ableToSpendToday.trSum, currencyCode())
                                                setColor(
                                                    requireContext().getColorFromAttr(
                                                        if (ableToSpendToday.trSum.setScale(
                                                                DECIMAL_LENGTH,
                                                                ROUND_HALF_UP
                                                            ).signum() > 0
                                                        ) R.attr.colorOnPrimaryContainer
                                                        else R.attr.colorError
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    expenseTodayInfoValue.show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun initCategoryRecyclerView() {
        viewBinding?.run {
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
        viewBinding?.expenseTransactionLayoutInclude?.run {
            val layoutManager = LinearLayoutManager(requireContext())
            expenseTransactionRvList.layoutManager = layoutManager
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration = StickyHeaderItemDecorator(transactionAdapter)
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            val dividerItemDecoration = DividerItemDecoration(
                expenseTransactionRvList.context,
                layoutManager.orientation
            )
            val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rv_divider)
            dividerDrawable?.let { dividerItemDecoration.setDrawable(it) }
            expenseTransactionRvList.addItemDecoration(dividerItemDecoration)
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
        viewBinding?.run {
            val inputSum = getSum()
            val action = if (inputSum.signum() > 0) {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MainWithSumScreen,
                    transactionType = TransactionType.EXPENSE
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MainScreen,
                    transactionType = TransactionType.EXPENSE
                )
            }
            findNavController().navigate(action)
        }
    }

    override fun saveTransaction(category: Category) {
        if (!category.type.isIncome()) {
            viewModel.saveTransaction(category, getSum(), getCurrencyCode())
        }
    }

    override fun getType() = TransactionType.EXPENSE

    companion object {
        fun newInstance() = ExpenseFragment()

        const val FRAGMENT_ID = 0L
    }
}
