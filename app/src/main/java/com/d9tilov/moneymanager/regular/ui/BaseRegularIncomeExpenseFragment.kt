package com.d9tilov.moneymanager.regular.ui

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
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseFragment
import com.d9tilov.moneymanager.base.ui.navigator.BaseRegularIncomeExpenseNavigator
import com.d9tilov.moneymanager.core.events.OnItemClickListener
import com.d9tilov.moneymanager.core.events.OnItemSwipeListener
import com.d9tilov.moneymanager.core.util.hideKeyboard
import com.d9tilov.moneymanager.core.util.show
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.regular.vm.BaseRegularIncomeExpenseViewModel
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.isIncome
import com.d9tilov.moneymanager.transaction.ui.TransactionRemoveDialog
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseRegularIncomeExpenseFragment<N : BaseRegularIncomeExpenseNavigator>(
    @LayoutRes layoutId: Int
) : BaseFragment<N>(layoutId), BaseRegularIncomeExpenseNavigator {

    protected val regularTransactionAdapter: RegularTransactionAdapter = RegularTransactionAdapter()
    protected abstract val transactionType: TransactionType
    protected abstract fun showBackButton(): Boolean
    protected var toolbar: MaterialToolbar? = null
    protected var emptyViewStub: ViewStub? = null

    private val onItemClickListener = object : OnItemClickListener<RegularTransaction> {
        override fun onItemClick(item: RegularTransaction, position: Int) {
            val action = if (transactionType.isIncome()) {
                RegularIncomeFragmentDirections.toCreateRegularTransactionDest(
                    transactionType,
                    item
                )
            } else {
                RegularExpenseFragmentDirections.toCreateRegularTransactionDest(
                    transactionType,
                    item
                )
            }

            findNavController().navigate(action)
        }
    }
    private val onItemSwipeListener = object : OnItemSwipeListener<RegularTransaction> {
        override fun onItemSwiped(item: RegularTransaction, position: Int) {
            openRemoveConfirmationDialog(item)
        }
    }
    private val onCheckBoxClickListener = object : OnItemClickListener<RegularTransaction> {
        override fun onItemClick(item: RegularTransaction, position: Int) {
            (viewModel as BaseRegularIncomeExpenseViewModel<N>).onCheckClicked(item)
        }
    }

    private fun openRemoveConfirmationDialog(transaction: RegularTransaction) {
        val action = if (transactionType.isIncome()) {
            RegularIncomeFragmentDirections.toRemoveTransactionDialog(regularTransaction = transaction)
        } else {
            RegularExpenseFragmentDirections.toRemoveTransactionDialog(regularTransaction = transaction)
        }
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        regularTransactionAdapter.itemClickListener = onItemClickListener
        regularTransactionAdapter.checkBoxClickListener = onCheckBoxClickListener
        regularTransactionAdapter.itemSwipeListener = onItemSwipeListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            TransactionRemoveDialog.ARG_UNDO_REMOVE_LAYOUT_DISMISS
        )?.observe(
            viewLifecycleOwner
        ) { if (it) regularTransactionAdapter.cancelDeletion() }
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
            getString(if (transactionType == TransactionType.INCOME) R.string.title_prepopulate_regular_incomes else R.string.title_prepopulate_regular_expenses)
        activity.setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        if (showBackButton()) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
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
            emptyViewStub?.show()
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
                    if (transactionType.isIncome())
                        R.string.transaction_empty_placeholder_regular_income_title else
                        R.string.transaction_empty_placeholder_regular_expense_title
                )
            val stubSubTitle =
                inflatedStub?.findViewById<TextView>(R.id.empty_placeholder_subtitle)
            stubSubTitle?.show()
            stubSubTitle?.text = getString(R.string.transaction_empty_placeholder_subtitle)
            val addExpense =
                inflatedStub?.findViewById<ImageView>(R.id.empty_placeholder_add)
            addExpense?.show()
            addExpense?.setOnClickListener {
                openCreatedScreen(transactionType)
            }
        }
    }

    protected fun hideViewStub() {
        /* don't use gone() */
        emptyViewStub?.visibility = View.GONE
    }

    private fun openCreatedScreen(transactionType: TransactionType) {
        val action = if (transactionType.isIncome()) {
            RegularIncomeFragmentDirections.toCreateRegularTransactionDest(TransactionType.INCOME)
        } else {
            RegularExpenseFragmentDirections.toCreateRegularTransactionDest(TransactionType.EXPENSE)
        }
        findNavController().navigate(action)
    }
}
