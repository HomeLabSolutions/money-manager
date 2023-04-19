package com.d9tilov.android.incomeexpense.income.ui

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
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.android.category.ui.navigation.CategoryDestination
import com.d9tilov.android.category.data.model.Category
import com.d9tilov.android.common_android.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.android.common_android.ui.recyclerview.ItemSnapHelper
import com.d9tilov.android.common_android.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import com.d9tilov.android.common_android.ui.base.currencyCode
import com.d9tilov.moneymanager.incomeexpense.ui.BaseIncomeExpenseFragment
import com.d9tilov.android.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.database.model.isIncome
import com.d9tilov.android.transaction.ui.callback.TransactionSwipeToDeleteCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IncomeFragment :
    BaseIncomeExpenseFragment<IncomeNavigator, FragmentIncomeBinding>(
        FragmentIncomeBinding::inflate,
        R.layout.fragment_income
    ),
    IncomeNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeViewModel>()
    override val snackBarAnchorView by lazy { categoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun initViews() {
        viewBinding?.run {
            emptyViewStub = incomeTransactionEmptyPlaceholderInclude
            categoryRvList = incomeCategoryRvList
            transactionRvList = incomeTransactionLayoutInclude.incomeTransactionRvList
            transactionBtnAdd = incomeTransactionLayoutInclude.incomeTransactionBtnAdd
            infoLayout = incomeInfoLayoutInclude.root
            transactionsLayout = incomeTransactionLayoutInclude.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    viewModel.earnedInPeriod.collect { sum ->
                        if (sum.signum() == 0) viewBinding?.incomeInfoLayoutInclude?.incomePeriodInfoApproxSign?.gone()
                        else viewBinding?.incomeInfoLayoutInclude?.incomePeriodInfoApproxSign?.show()
                    }
                }
                launch {
                    viewModel.earnedInPeriodApprox.collect { sum ->
                        viewBinding?.incomeInfoLayoutInclude?.incomePeriodInfoApproxSum?.setValue(
                            sum,
                            currencyCode()
                        )
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
            incomeCategoryRvList.layoutManager = layoutManager
            incomeCategoryRvList.adapter = categoryAdapter
            incomeCategoryRvList.addItemDecoration(
                GridSpaceItemDecoration(
                    SPAN_COUNT,
                    resources.getDimension(R.dimen.recycler_view_category_offset).toInt(),
                    HORIZONTAL
                )
            )
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(incomeCategoryRvList)
        }
    }

    override fun initTransactionsRecyclerView() {
        viewBinding?.incomeTransactionLayoutInclude?.run {
            val layoutManager = LinearLayoutManager(requireContext())
            incomeTransactionRvList.layoutManager = layoutManager
            incomeTransactionRvList.layoutManager = layoutManager
            incomeTransactionRvList.adapter = transactionAdapter
            val itemDecoration = StickyHeaderItemDecorator(transactionAdapter)
            itemDecoration.attachToRecyclerView(incomeTransactionRvList)
            val dividerItemDecoration = DividerItemDecoration(
                incomeTransactionRvList.context,
                layoutManager.orientation
            )
            val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rv_divider)
            dividerDrawable?.let { dividerItemDecoration.setDrawable(it) }
            incomeTransactionRvList.addItemDecoration(dividerItemDecoration)
            ItemTouchHelper(object : com.d9tilov.android.transaction.ui.callback.TransactionSwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(incomeTransactionRvList)
        }
    }

    override fun openCategoriesScreen() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, "click_all_categories_income")
        }
        viewBinding.run {
            val inputSum = getSum()
            val action = if (inputSum.signum() > 0) {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MainWithSumScreen,
                    transactionType = TransactionType.INCOME
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MainScreen,
                    transactionType = TransactionType.INCOME
                )
            }
            findNavController().navigate(action)
        }
    }

    override fun saveTransaction(category: Category) {
        if (category.type.isIncome()) {
            viewModel.saveTransaction(category, getSum(), getCurrencyCode())
        }
    }

    override fun getType() = TransactionType.INCOME

    companion object {
        fun newInstance() = IncomeFragment()

        const val FRAGMENT_ID = 1L
    }
}
