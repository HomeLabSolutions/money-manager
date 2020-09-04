package com.d9tilov.moneymanager.transaction.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val args by navArgs<TransactionRemoveDialogFragmentArgs>()
    private val transaction by lazy { args.transaction }
    private val fixedTransaction by lazy { args.fixedTransaction }

    override val layoutId = R.layout.fragment_dialog_remove
    override fun performDataBinding(view: View) = FragmentDialogRemoveBinding.bind(view)
    override fun getNavigator() = this
    override val viewModel by viewModels<RemoveTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            transactionRemoveDialogTitle.text = getString(R.string.transaction_delete_dialog_title)
            transactionRemoveDialogSubtitle.text =
                getString(R.string.transaction_delete_dialog_subtitle)
            transactionRemoveButtonConfirm.setOnClickListener {
                transaction?.let { viewModel.removeTransaction(it) }
                fixedTransaction?.let { viewModel.removeFixedTransaction(it) }
            }
            transactionRemoveButtonCancel.setOnClickListener {
                dismiss()
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ARG_UNDO_REMOVE_LAYOUT_DISMISS,
                    true
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
    }

    override fun remove() {
        dismiss()
        findNavController().popBackStack(R.id.edit_transaction_dest, true)
    }

    companion object {
        const val ARG_UNDO_REMOVE_LAYOUT_DISMISS = "undo_remove_layout_dismiss"
    }
}
