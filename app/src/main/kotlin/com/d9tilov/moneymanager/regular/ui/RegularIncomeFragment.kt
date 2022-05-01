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
import com.d9tilov.moneymanager.base.ui.navigator.RegularIncomeNavigator
import com.d9tilov.moneymanager.core.ui.recyclerview.MarginItemDecoration
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.databinding.FragmentRegularIncomeBinding
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransactionDestination
import com.d9tilov.moneymanager.regular.vm.RegularIncomeViewModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

    override fun showBackButton() = destination == RegularTransactionDestination.PROFILE_SCREEN
}
