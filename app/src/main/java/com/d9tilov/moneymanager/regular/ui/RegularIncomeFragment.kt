package com.d9tilov.moneymanager.regular.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.RegularIncomeNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentRegularIncomeBinding
import com.d9tilov.moneymanager.regular.RegularTransactionDestination
import com.d9tilov.moneymanager.regular.vm.RegularIncomeViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegularIncomeFragment :
    BaseRegularIncomeExpenseFragment<RegularIncomeNavigator>(R.layout.fragment_regular_income),
    RegularIncomeNavigator {

    private val args by navArgs<RegularIncomeFragmentArgs>()
    private val destination by lazy { args.destination }

    private val viewBinding by viewBinding(FragmentRegularIncomeBinding::bind)
    override val transactionType = TransactionType.INCOME
    override fun getNavigator() = this
    override val viewModel by viewModels<RegularIncomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            toolbar = regularIncomeToolbarContainer.toolbar
            regularIncomeRvList.adapter = regularTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            regularIncomeRvList.layoutManager = layoutManager
            regularIncomeRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_regular_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    regularTransactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(regularIncomeRvList)
        }
        emptyViewStub = viewBinding.root.findViewById(R.id.regular_income_empty_placeholder)
        viewModel.regularIncomeTransactionList.observe(
            this.viewLifecycleOwner
        ) {
            if (it.isEmpty()) {
                viewBinding.regularIncomeRvList.gone()
                showViewStub(TransactionType.INCOME)
            } else {
                hideViewStub()
                viewBinding.regularIncomeRvList.show()
                regularTransactionAdapter.updateItems(it)
            }
        }
    }

    override fun showBackButton() = destination == RegularTransactionDestination.PROFILE_SCREEN
}
