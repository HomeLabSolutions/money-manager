package com.d9tilov.moneymanager.incomeexpense.expense.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.callback.SwipeToDeleteCallback
import com.d9tilov.moneymanager.base.ui.navigator.ExpenseNavigator
import com.d9tilov.moneymanager.base.ui.recyclerview.ItemSnapHelper
import com.d9tilov.moneymanager.base.ui.recyclerview.decoration.SpaceItemDecoration
import com.d9tilov.moneymanager.base.ui.recyclerview.decoration.StickyHeaderItemDecorator
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.category.ui.CategoryAdapter
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.events.OnKeyboardVisibleChange
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.showKeyboard
import com.d9tilov.moneymanager.databinding.FragmentExpenseBinding
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.TransactionAdapter
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.math.BigDecimal

@AndroidEntryPoint
class ExpenseFragment :
    BaseFragment<FragmentExpenseBinding, ExpenseNavigator>(R.layout.fragment_expense),
    ExpenseNavigator,
    OnKeyboardVisibleChange {

    private val categoryAdapter = CategoryAdapter()
    private val transactionAdapter = TransactionAdapter()
    private val onItemClickListener = object : OnItemClickListener<Category> {
        override fun onItemClick(item: Category, position: Int) {
            if (item.id == Category.ALL_ITEMS_ID) {
                viewModel.openAllCategories()
            } else {
                viewBinding?.let {
                    viewModel.saveTransaction(item, it.expenseMainSum.getValue())
                    it.expenseMainSum.setValue(BigDecimal.ZERO)
                }
            }
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<Transaction> {
        override fun onItemSwiped(item: Transaction, position: Int) {
            viewModel.deleteTransaction(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryAdapter.itemClickListener = onItemClickListener
        transactionAdapter.itemSwipeListener = onItemSwipeListener
    }

    override fun onStart() {
        super.onStart()
        showKeyboard(viewBinding?.expenseMainSum)
    }

    override fun onStop() {
        hideKeyboard()
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoryRecyclerView()
        initTransactionsRecyclerView()
        viewModel.run {
            categories.observe(
                viewLifecycleOwner,
                Observer { categoryAdapter.updateItems(it) }
            )
            transactions.observe(
                viewLifecycleOwner,
                Observer { transactionAdapter.submitList(it) }
            )
            addTransactionEvent.observe(
                viewLifecycleOwner,
                Observer { hideKeyboard(viewBinding?.expenseMainSum?.moneyEditText) }
            )
        }
    }

    fun cancelDelete() {
        transactionAdapter.cancelDeletion()
    }

    private fun initCategoryRecyclerView() {
        viewBinding?.run {
            expenseCategoryRvList.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
            expenseCategoryRvList.adapter = categoryAdapter
            expenseCategoryRvList.addItemDecoration(
                SpaceItemDecoration(
                    requireContext(),
                    R.dimen.recycler_view_category_offset
                )
            )
            val snapHelper: SnapHelper = ItemSnapHelper()
            snapHelper.attachToRecyclerView(expenseCategoryRvList)
        }
    }

    private fun initTransactionsRecyclerView() {
        viewBinding?.run {
            expenseTransactionRvList.layoutManager =
                LinearLayoutManager(requireContext())
            expenseTransactionRvList.adapter = transactionAdapter
            val itemDecoration = StickyHeaderItemDecorator(transactionAdapter)
            itemDecoration.attachToRecyclerView(expenseTransactionRvList)
            ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    transactionAdapter.deleteItem(viewHolder.adapterPosition)
                }
            }).attachToRecyclerView(expenseTransactionRvList)
        }
    }

    override fun performDataBinding(view: View): FragmentExpenseBinding =
        FragmentExpenseBinding.bind(view)

    override fun getNavigator() = this
    override val viewModel by viewModels<ExpenseViewModel>()
    override fun openCategoriesScreen() {
        findNavController().navigate(R.id.action_mainFragment_to_category_dest)
    }

    override fun openRemoveConfirmationDialog(transaction: Transaction) {
        TransactionRemoveDialog.newInstance(transaction).let {
            it.show(childFragmentManager, it.tag)
        }
    }

    override fun onOpenKeyboard() {
        Timber.tag(App.TAG).d("keyboard shown")
        viewBinding?.run {
            expenseTransactionRvList.visibility = INVISIBLE
            expenseCategoryRvList.visibility = VISIBLE
        }
    }

    override fun onCloseKeyboard() {
        Timber.tag(App.TAG).d("keyboard hidden")
        viewBinding?.run {
            expenseTransactionRvList.visibility = VISIBLE
            expenseCategoryRvList.visibility = GONE
        }
    }

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}
