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
import com.d9tilov.moneymanager.core.ui.viewbinding.viewBinding
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import com.d9tilov.moneymanager.transaction.ui.vm.RemoveTransactionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionRemoveDialog :
    BaseDialogFragment<RemoveTransactionDialogNavigator>(),
    RemoveTransactionDialogNavigator {

    private val args by navArgs<TransactionRemoveDialogArgs>()
    private val transaction by lazy { args.transaction }
    private val regularTransaction by lazy { args.regularTransaction }
    private val viewBinding by viewBinding(FragmentDialogRemoveBinding::bind)

    override val layoutId = R.layout.fragment_dialog_remove
    override fun getNavigator() = this
    override val viewModel by viewModels<RemoveTransactionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.run {
            removeDialogTitle.text = getString(R.string.transaction_delete_dialog_title)
            removeDialogSubtitle.text =
                getString(R.string.transaction_delete_dialog_subtitle)
            removeButtonConfirm.setOnClickListener {
                transaction?.let { viewModel.removeTransaction(it) }
                regularTransaction?.let { viewModel.removeRegularTransaction(it) }
            }
            removeButtonCancel.setOnClickListener {
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
        const val ARG_UNDO_REMOVE_LAYOUT_DISMISS = "arg_undo_remove_layout_dismiss"
    }
}
