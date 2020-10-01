package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.FixedIncomeNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.databinding.FragmentFixedIncomeBinding
import com.d9tilov.moneymanager.fixed.vm.FixedIncomeViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedIncomeFragment :
    BaseFixedIncomeExpenseFragment<FragmentFixedIncomeBinding, FixedIncomeNavigator>(R.layout.fragment_fixed_income),
    FixedIncomeNavigator {

    override val transactionType = TransactionType.INCOME
    override fun performDataBinding(view: View) = FragmentFixedIncomeBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<FixedIncomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            toolbar = fixedIncomeToolbarContainer.toolbar
            fixedIncomeRvList.adapter = fixedTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            fixedIncomeRvList.layoutManager = layoutManager
            fixedIncomeRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    fixedTransactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(fixedIncomeRvList)
        }
        emptyViewStub = viewBinding?.root?.findViewById(R.id.fixed_income_empty_placeholder)
        viewModel.fixedIncomeTransactionList.observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding?.fixedIncomeRvList?.visibility = GONE
                    showViewStub(TransactionType.INCOME)
                } else {
                    hideViewStub()
                    viewBinding?.fixedIncomeRvList?.visibility = VISIBLE
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }
}
