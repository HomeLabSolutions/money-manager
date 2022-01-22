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
import com.d9tilov.moneymanager.base.ui.navigator.RegularExpenseNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentRegularExpenseBinding
import com.d9tilov.moneymanager.regular.RegularTransactionDestination
import com.d9tilov.moneymanager.regular.vm.RegularExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegularExpenseFragment :
    BaseRegularIncomeExpenseFragment<RegularExpenseNavigator>(R.layout.fragment_regular_expense),
    RegularExpenseNavigator {

    private val args by navArgs<RegularExpenseFragmentArgs>()
    private val destination by lazy { args.destination }

    private val viewBinding by viewBinding(FragmentRegularExpenseBinding::bind)
    override val transactionType = TransactionType.EXPENSE
    override fun getNavigator() = this
    override val viewModel by viewModels<RegularExpenseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            toolbar = regularExpenseToolbarContainer.toolbar
            regularExpenseRvList.adapter = regularTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            regularExpenseRvList.layoutManager = layoutManager
            regularExpenseRvList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.recycler_view_regular_category_margin).toInt()
                )
            )
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    regularTransactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(regularExpenseRvList)
        }
        emptyViewStub = viewBinding.root.findViewById(R.id.regular_expense_empty_placeholder)
        viewModel.regularExpenseTransactionList.observe(
            this.viewLifecycleOwner
        ) {
            if (it.isEmpty()) {
                viewBinding.regularExpenseRvList.gone()
                showViewStub(TransactionType.EXPENSE)
            } else {
                hideViewStub()
                viewBinding.regularExpenseRvList.show()
                regularTransactionAdapter.updateItems(it)
            }
        }
    }

    override fun showBackButton() = destination == RegularTransactionDestination.PROFILE_SCREEN
}
