package com.d9tilov.moneymanager.periodic.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.PeriodicExpenseNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentPeriodicExpenseBinding
import com.d9tilov.moneymanager.periodic.vm.PeriodicExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeriodicExpenseFragment :
    BasePeriodicIncomeExpenseFragment<PeriodicExpenseNavigator>(R.layout.fragment_periodic_expense),
    PeriodicExpenseNavigator {

    private val viewBinding by viewBinding(FragmentPeriodicExpenseBinding::bind)
    override val transactionType = TransactionType.EXPENSE
    override fun getNavigator() = this
    override val viewModel by viewModels<PeriodicExpenseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            toolbar = periodicExpenseToolbarContainer.toolbar
            periodicExpenseRvList.adapter = fixedTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            periodicExpenseRvList.layoutManager = layoutManager
            periodicExpenseRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    fixedTransactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(periodicExpenseRvList)
        }
        emptyViewStub = viewBinding.root.findViewById(R.id.periodic_expense_empty_placeholder)
        viewModel.getFixedExpenseTransactionList().observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding.periodicExpenseRvList.gone()
                    showViewStub(TransactionType.EXPENSE)
                } else {
                    hideViewStub()
                    viewBinding.periodicExpenseRvList.show()
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }
}
