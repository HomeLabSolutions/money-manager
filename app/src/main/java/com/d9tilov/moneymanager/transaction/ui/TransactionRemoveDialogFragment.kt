package com.d9tilov.moneymanager.transaction.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveTransactionDialogNavigator
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import com.d9tilov.moneymanager.transaction.ui.vm.RemoveTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionRemoveDialogFragment :
    BaseDialogFragment<FragmentDialogRemoveBinding, RemoveTransactionDialogNavigator>(),
    RemoveTransactionDialogNavigator {
    override val layoutId = R.layout.fragment_dialog_remove

    override fun performDataBinding(view: View) = FragmentDialogRemoveBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<RemoveTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            transactionRemoveDialogTitle.text = getString(R.string.remove_transaction_dialog_title)
            transactionRemoveDialogSubtitle.text =
                getString(R.string.remove_transaction_dialog_subtitle)
            transactionRemoveButtonConfirm.setOnClickListener { viewModel.remove() }
            transactionRemoveButtonCancel.setOnClickListener { viewModel.cancel() }
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
        val action = TransactionRemoveDialogFragmentDirections.toIncomeExpenseDest()
        findNavController().navigate(action)
    }
}
