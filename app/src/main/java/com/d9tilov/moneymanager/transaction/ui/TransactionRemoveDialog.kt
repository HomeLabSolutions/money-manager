package com.d9tilov.moneymanager.transaction.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveTransactionNavigator
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.ui.vm.RemoveTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionRemoveDialog :
    BaseDialogFragment<FragmentDialogRemoveBinding, RemoveTransactionNavigator>(),
    RemoveTransactionNavigator {
    override val layoutId = R.layout.fragment_dialog_remove

    override fun performDataBinding(view: View) = FragmentDialogRemoveBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<RemoveTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            dialogTitle.text = getString(R.string.remove_transaction_dialog_title)
            dialogSubtitle.text = getString(R.string.remove_transaction_dialog_subtitle)
            buttonConfirm.setOnClickListener { viewModel.remove() }
            buttonCancel.setOnClickListener { viewModel.cancel() }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
    }

    override fun remove() {
        dismiss()
    }

    override fun cancel() {
        dismiss()
        (parentFragment as ExpenseFragment).cancelDelete()
    }

    companion object {
        const val ARG_REMOVE_TRANSACTION = "REMOVE_TRANSACTION"

        fun newInstance(transaction: Transaction) = TransactionRemoveDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_REMOVE_TRANSACTION, transaction)
            }
        }
    }
}
