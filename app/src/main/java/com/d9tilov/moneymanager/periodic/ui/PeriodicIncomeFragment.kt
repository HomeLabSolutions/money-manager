package com.d9tilov.moneymanager.periodic.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.PeriodicIncomeNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentPeriodicIncomeBinding
import com.d9tilov.moneymanager.periodic.vm.PeriodicIncomeViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeriodicIncomeFragment :
    BasePeriodicIncomeExpenseFragment<PeriodicIncomeNavigator>(R.layout.fragment_periodic_income),
    PeriodicIncomeNavigator {

    private val viewBinding by viewBinding(FragmentPeriodicIncomeBinding::bind)
    override val transactionType = TransactionType.INCOME
    override fun getNavigator() = this
    override val viewModel by viewModels<PeriodicIncomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            toolbar = periodicIncomeToolbarContainer.toolbar
            periodicIncomeRvList.adapter = fixedTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            periodicIncomeRvList.layoutManager = layoutManager
            periodicIncomeRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    fixedTransactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(periodicIncomeRvList)
        }
        emptyViewStub = viewBinding.root.findViewById(R.id.periodic_income_empty_placeholder)
        viewModel.getFixedIncomeTransactionList().observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding.periodicIncomeRvList.gone()
                    showViewStub(TransactionType.INCOME)
                } else {
                    hideViewStub()
                    viewBinding.periodicIncomeRvList.show()
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }
}
