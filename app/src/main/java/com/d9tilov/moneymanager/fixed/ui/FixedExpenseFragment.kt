package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.FixedExpenseNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentFixedExpenseBinding
import com.d9tilov.moneymanager.fixed.vm.FixedExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedExpenseFragment :
    BaseFixedIncomeExpenseFragment<FixedExpenseNavigator>(R.layout.fragment_fixed_expense),
    FixedExpenseNavigator {

    private val viewBinding by viewBinding(FragmentFixedExpenseBinding::bind)
    override val transactionType = TransactionType.EXPENSE
    override fun getNavigator() = this
    override val viewModel by viewModels<FixedExpenseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            toolbar = fixedExpenseToolbarContainer.toolbar
            fixedExpenseRvList.adapter = fixedTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            fixedExpenseRvList.layoutManager = layoutManager
            fixedExpenseRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_fixed_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    fixedTransactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(fixedExpenseRvList)
        }
        emptyViewStub = viewBinding.root.findViewById(R.id.fixed_expense_empty_placeholder)
        viewModel.getFixedExpenseTransactionList().observe(
            this.viewLifecycleOwner,
            Observer {
                if (it.isEmpty()) {
                    viewBinding.fixedExpenseRvList.gone()
                    showViewStub(TransactionType.EXPENSE)
                } else {
                    hideViewStub()
                    viewBinding.fixedExpenseRvList.show()
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }
}
