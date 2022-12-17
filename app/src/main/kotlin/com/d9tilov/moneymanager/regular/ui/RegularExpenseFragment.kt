package com.d9tilov.moneymanager.regular.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.RegularExpenseNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentRegularExpenseBinding
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransactionDestination
import com.d9tilov.moneymanager.regular.vm.RegularExpenseViewModel
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegularExpenseFragment :
    BaseRegularIncomeExpenseFragment<RegularExpenseNavigator, FragmentRegularExpenseBinding>(
        FragmentRegularExpenseBinding::inflate,
        R.layout.fragment_regular_expense
    ),
    RegularExpenseNavigator {

    private val args by navArgs<RegularExpenseFragmentArgs>()
    private val destination by lazy { args.destination }

    override val transactionType = TransactionType.EXPENSE
    override fun getNavigator() = this
    override val viewModel by viewModels<RegularExpenseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
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
        emptyViewStub = viewBinding?.regularExpenseEmptyPlaceholder
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.regularExpenseTransactionList
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { list ->
                    if (list.isEmpty()) {
                        viewBinding?.regularExpenseRvList?.gone()
                        showViewStub(TransactionType.EXPENSE)
                    } else {
                        hideViewStub()
                        viewBinding?.regularExpenseRvList?.show()
                        regularTransactionAdapter.updateItems(list)
                    }
                }
        }
    }

    override fun showBackButton() = destination == RegularTransactionDestination.ProfileScreen
}
