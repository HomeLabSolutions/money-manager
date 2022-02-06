package com.d9tilov.moneymanager.incomeexpense.income.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.navigator.IncomeNavigator
import com.d9tilov.moneymanager.category.CategoryDestination
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.ui.recyclerview.GridSpaceItemDecoration
import com.d9tilov.moneymanager.core.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.core.ui.recyclerview.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.isTablet
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentIncomeBinding
import com.d9tilov.moneymanager.incomeexpense.ui.BaseIncomeExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragmentDirections
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.callback.TransactionSwipeToDeleteCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IncomeFragment :
    BaseIncomeExpenseFragment<IncomeNavigator>(R.layout.fragment_income),
    IncomeNavigator {

    private val viewBinding by viewBinding(FragmentIncomeBinding::bind)

    override fun getNavigator() = this
    override val viewModel by viewModels<IncomeViewModel>()
    override val snackBarAnchorView by lazy { categoryRvList }

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun initViews() {
        emptyViewStub = viewBinding.root.findViewById(R.id.income_transaction_empty_placeholder)
        categoryRvList = viewBinding.incomeInfoLayoutInclude.incomeCategoryRvList
        transactionRvList = viewBinding.incomeTransactionLayoutInclude.incomeTransactionRvList
        transactionBtnAdd = viewBinding.incomeTransactionLayoutInclude.incomeTransactionBtnAdd
        infoLayout = viewBinding.incomeInfoLayoutInclude.root
        transactionsLayout = viewBinding.incomeTransactionLayoutInclude.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            categories.observe(viewLifecycleOwner) { list ->
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
        lifecycleScope.launch {
            viewModel.transactions.collectLatest { transactionAdapter.submitData(it) }
        }
        lifecycleScope.launch {
            viewModel.earnedInPeriod.observe(
                viewLifecycleOwner
            ) { sum ->
                if (sum.signum() == 0) {
                    viewBinding.incomeInfoLayoutInclude.incomePeriodInfoApproxSign.gone()
                } else
                    viewBinding.incomeInfoLayoutInclude.incomePeriodInfoApproxSign.show()
            }
            viewModel.earnedInPeriodApprox.observe(
                viewLifecycleOwner
            ) { sum -> viewBinding.incomeInfoLayoutInclude.incomePeriodInfoApproxSum.setValue(sum) }
        }
    }

    override fun initCategoryRecyclerView() {
        viewBinding.incomeInfoLayoutInclude.run {
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
        viewBinding.incomeTransactionLayoutInclude.run {
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
            ItemTouchHelper(object : TransactionSwipeToDeleteCallback(requireContext()) {
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
                    destination = CategoryDestination.MAIN_WITH_SUM_SCREEN,
                    transactionType = TransactionType.INCOME
                )
            } else {
                IncomeExpenseFragmentDirections.toCategoryDest(
                    destination = CategoryDestination.MAIN_SCREEN,
                    transactionType = TransactionType.INCOME
                )
            }
            findNavController().navigate(action)
        }
    }

    override fun saveTransaction(category: Category) {
        if (category.type == TransactionType.INCOME) {
            viewModel.saveTransaction(category, getSum(), getCurrencyCode())
        }
    }

    override fun getType() = TransactionType.INCOME

    companion object {
        fun newInstance() = IncomeFragment()

        const val FRAGMENT_ID = 1L
    }
}
