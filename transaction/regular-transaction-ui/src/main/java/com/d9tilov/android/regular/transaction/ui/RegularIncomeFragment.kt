package com.d9tilov.android.regular.transaction.ui

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
import com.d9tilov.android.common.android.ui.recyclerview.MarginItemDecoration
import com.d9tilov.android.common.android.ui.recyclerview.SwipeToDeleteCallback
import com.d9tilov.android.common.android.utils.gone
import com.d9tilov.android.common.android.utils.show
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.regular.transaction.ui.navigator.RegularIncomeNavigator
import com.d9tilov.android.regular.transaction.ui.vm.RegularIncomeViewModel
import com.d9tilov.android.regular_transaction_ui.R
import com.d9tilov.android.regular_transaction_ui.databinding.FragmentRegularIncomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegularIncomeFragment :
    BaseRegularIncomeExpenseFragment<RegularIncomeNavigator, FragmentRegularIncomeBinding>(
        FragmentRegularIncomeBinding::inflate,
        R.layout.fragment_regular_income
    ),
    RegularIncomeNavigator {

    private val args by navArgs<RegularIncomeFragmentArgs>()
    private val destination by lazy { args.destination }

    override val transactionType = TransactionType.INCOME
    override fun getNavigator() = this
    override val viewModel by viewModels<RegularIncomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            toolbar = regularIncomeToolbarContainer.toolbar
            regularIncomeRvList.adapter = regularTransactionAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            regularIncomeRvList.layoutManager = layoutManager
            regularIncomeRvList.addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.recycler_view_regular_category_margin).toInt()))
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    regularTransactionAdapter.deleteItem(viewHolder.bindingAdapterPosition)
                }
            }).attachToRecyclerView(regularIncomeRvList)
            emptyViewStub = regularIncomeEmptyPlaceholder
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.regularIncomeTransactionList
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { list ->
                        if (list.isEmpty()) {
                            regularIncomeRvList.gone()
                            showViewStub(TransactionType.INCOME)
                        } else {
                            hideViewStub()
                            regularIncomeRvList.show()
                            regularTransactionAdapter.updateItems(list)
                        }
                    }
            }
        }
    }

    override fun showBackButton() = destination == com.d9tilov.android.regular.transaction.domain.model.RegularTransactionDestination.ProfileScreen
}
