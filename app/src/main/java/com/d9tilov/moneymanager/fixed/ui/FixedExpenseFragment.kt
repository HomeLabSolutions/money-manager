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
import com.d9tilov.moneymanager.base.ui.navigator.FixedExpenseNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.databinding.FragmentFixedExpenseBinding
import com.d9tilov.moneymanager.fixed.vm.FixedExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedExpenseFragment :
    BaseFixedIncomeExpenseFragment<FragmentFixedExpenseBinding, FixedExpenseNavigator>(R.layout.fragment_fixed_expense),
    FixedExpenseNavigator {

    override val transactionType = TransactionType.EXPENSE
    override fun performDataBinding(view: View) = FragmentFixedExpenseBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<FixedExpenseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.let {
            toolbar = it.fixedExpenseToolbarContainer.toolbar
            it.fixedExpenseRvList.adapter = fixedTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            it.fixedExpenseRvList.layoutManager = layoutManager
            it.fixedExpenseRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    fixedTransactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(it.fixedExpenseRvList)
        }
        emptyViewStub = viewBinding?.root?.findViewById(R.id.fixed_expense_empty_placeholder)
        viewModel.fixedExpenseTransactionList.observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding?.fixedExpenseRvList?.visibility = GONE
                    showViewStub(TransactionType.EXPENSE)
                } else {
                    hideViewStub()
                    viewBinding?.fixedExpenseRvList?.visibility = VISIBLE
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }
}
