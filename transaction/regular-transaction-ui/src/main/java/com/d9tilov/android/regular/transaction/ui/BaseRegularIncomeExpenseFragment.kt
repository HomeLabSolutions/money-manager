package com.d9tilov.android.regular.transaction.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.d9tilov.android.designsystem.databinding.LayoutEmptyListPlaceholderBinding
import com.d9tilov.android.common_android.ui.base.BaseFragment
import com.d9tilov.android.common_android.ui.base.Inflate
import com.d9tilov.android.common_android.utils.gone
import com.d9tilov.android.common_android.utils.show
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.model.isIncome
import com.d9tilov.android.regular.transaction.domain.model.RegularTransaction
import com.d9tilov.android.regular.transaction.ui.navigator.BaseRegularIncomeExpenseNavigator
import com.d9tilov.android.regular.transaction.ui.vm.BaseRegularIncomeExpenseViewModel
import com.d9tilov.android.regular_transaction_ui.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseRegularIncomeExpenseFragment<N : BaseRegularIncomeExpenseNavigator, VB : ViewBinding>(
    inflate: Inflate<VB>,
    @LayoutRes layoutId: Int
) :
    BaseFragment<N, VB>(inflate, layoutId),
    BaseRegularIncomeExpenseNavigator {

    protected val regularTransactionAdapter: RegularTransactionAdapter = RegularTransactionAdapter(
        { item, _ ->
            // need to implement
        },
        { item, _ -> (viewModel as BaseRegularIncomeExpenseViewModel<N>).onCheckClicked(item) },
        { item, _ -> openRemoveConfirmationDialog(item) }
    )
    protected abstract val transactionType: TransactionType
    protected abstract fun showBackButton(): Boolean
    protected var toolbar: MaterialToolbar? = null
    protected var emptyViewStub: LayoutEmptyListPlaceholderBinding? = null

    private fun openRemoveConfirmationDialog(transaction: RegularTransaction) {
        //need to implement
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.prepopulate_menu, menu)
                    menu.findItem(R.id.action_skip).isVisible = false
                    menu.findItem(R.id.action_add).isVisible = true
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.action_add -> openCreatedScreen(transactionType)
                        else -> throw IllegalArgumentException("Unknown menu item with id: ${menuItem.itemId}")
                    }
                    return true
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    override fun onStart() {
        super.onStart()
        initToolbar()
    }

    private fun initToolbar() {
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        toolbar?.title =
            getString(if (transactionType == TransactionType.INCOME) R.string.title_prepopulate_regular_incomes else R.string.title_prepopulate_regular_expenses)
        if (showBackButton()) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun showViewStub(transactionType: TransactionType) {
        emptyViewStub?.let {
            it.root.show()
            it.emptyPlaceholderIcon.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_wallet_empty)
            )
            it.emptyPlaceholderTitle.text =
                getString(
                    if (transactionType.isIncome()) R.string.transaction_empty_placeholder_regular_income_title
                    else R.string.transaction_empty_placeholder_regular_expense_title
                )
            it.emptyPlaceholderSubtitle.show()
        }
    }

    protected fun hideViewStub() {
        emptyViewStub?.root?.gone()
    }

    private fun openCreatedScreen(transactionType: TransactionType) {
        // need to implement
    }
}
