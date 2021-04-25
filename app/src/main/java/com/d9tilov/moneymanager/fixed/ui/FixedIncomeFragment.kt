package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.FixedIncomeNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentFixedIncomeBinding
import com.d9tilov.moneymanager.fixed.vm.FixedIncomeViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FixedIncomeFragment :
    BaseFixedIncomeExpenseFragment<FixedIncomeNavigator>(R.layout.fragment_fixed_income),
    FixedIncomeNavigator {

    private val viewBinding by viewBinding(FragmentFixedIncomeBinding::bind)
    override val transactionType = TransactionType.INCOME
    override fun getNavigator() = this
    override val viewModel by viewModels<FixedIncomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
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
        emptyViewStub = viewBinding.root.findViewById(R.id.fixed_income_empty_placeholder)
        viewModel.getFixedIncomeTransactionList().observe(
            this.viewLifecycleOwner,
            {
                if (it.isEmpty()) {
                    viewBinding.fixedIncomeRvList.gone()
                    showViewStub(TransactionType.INCOME)
                } else {
                    hideViewStub()
                    viewBinding.fixedIncomeRvList.show()
                    fixedTransactionAdapter.updateItems(it)
                }
            }
        )
    }
}
