package com.d9tilov.moneymanager.fixed.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BaseFixedIncomeExpenseNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.fixed.vm.BaseFixedIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialog
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseFixedIncomeExpenseFragment<N : BaseFixedIncomeExpenseNavigator>(
    @LayoutRes layoutId: Int
) :
    BaseFragment<N>(layoutId), BaseFixedIncomeExpenseNavigator {

    protected val fixedTransactionAdapter: FixedTransactionAdapter = FixedTransactionAdapter()
    protected abstract val transactionType: TransactionType
    protected var toolbar: MaterialToolbar? = null
    protected var emptyViewStub: ViewStub? = null

    private val onItemClickListener = object : OnItemClickListener<FixedTransaction> {
        override fun onItemClick(item: FixedTransaction, position: Int) {
            val action = if (transactionType == TransactionType.INCOME) {
                FixedIncomeFragmentDirections.toCreateFixedTransactionDest(
                    transactionType,
                    item
                )
            } else {
                FixedExpenseFragmentDirections.toCreateFixedTransactionDest(
                    transactionType,
                    item
                )
            }

            findNavController().navigate(action)
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<FixedTransaction> {
        override fun onItemSwiped(item: FixedTransaction, position: Int) {
            openRemoveConfirmationDialog(item)
        }
    }
    private val onCheckBoxClickListener = object : OnItemClickListener<FixedTransaction> {
        override fun onItemClick(item: FixedTransaction, position: Int) {
            (viewModel as BaseFixedIncomeExpenseViewModel<N>).onCheckClicked(item)
        }
    }

    private fun openRemoveConfirmationDialog(transaction: FixedTransaction) {
        val action = if (transactionType == TransactionType.INCOME) {
            FixedIncomeFragmentDirections.toRemoveTransactionDialog(fixedTransaction = transaction)
        } else {
            FixedExpenseFragmentDirections.toRemoveTransactionDialog(fixedTransaction = transaction)
        }
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixedTransactionAdapter.itemClickListener = onItemClickListener
        fixedTransactionAdapter.checkBoxClickListener = onCheckBoxClickListener
        fixedTransactionAdapter.itemSwipeListener = onItemSwipeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            TransactionRemoveDialog.ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(
            viewLifecycleOwner
        ) {
            if (it) {
                fixedTransactionAdapter.cancelDeletion()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.prepopulate_menu, menu)
        menu.findItem(R.id.action_skip).isVisible = false
        menu.findItem(R.id.action_add).isVisible = true
    }

    override fun onStart() {
        super.onStart()
        initToolbar()
        hideKeyboard()
    }

    private fun initToolbar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title =
            getString(if (transactionType == TransactionType.INCOME) R.string.title_prepopulate_fixed_incomes else R.string.title_prepopulate_fixed_expenses)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    openCreatedScreen(transactionType)
                }
                else -> throw IllegalArgumentException("Unknown menu item with id: ${it.itemId}")
            }
            return@setOnMenuItemClickListener false
        }
    }

    protected fun showViewStub(transactionType: TransactionType) {
        if (emptyViewStub?.parent == null) {
            emptyViewStub?.visibility = View.VISIBLE
        } else {
            val inflatedStub = emptyViewStub?.inflate()
            val stubIcon =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_icon)
            stubIcon?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_wallet_empty
                )
            )
            val stubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_title)
            stubTitle?.text =
                getString(
                    if (transactionType == TransactionType.INCOME)
                        R.string.transaction_empty_placeholder_fixed_income_title else
                        R.string.transaction_empty_placeholder_fixed_expense_title
                )
            val stubSubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_subtitle)
            stubSubTitle?.visibility = View.VISIBLE
            stubSubTitle?.text = getString(R.string.transaction_empty_placeholder_subtitle)
            val addExpense =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_add)
            addExpense?.visibility = View.VISIBLE
            addExpense?.setOnClickListener {
                openCreatedScreen(transactionType)
            }
        }
    }

    protected fun hideViewStub() {
        emptyViewStub?.gone()
    }

    private fun openCreatedScreen(transactionType: TransactionType) {
        val action = if (transactionType == TransactionType.INCOME) {
            FixedIncomeFragmentDirections.toCreateFixedTransactionDest(TransactionType.INCOME)
        } else {
            FixedExpenseFragmentDirections.toCreateFixedTransactionDest(TransactionType.EXPENSE)
        }
        findNavController().navigate(action)
    }
}
