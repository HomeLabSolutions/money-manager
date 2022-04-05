package com.d9tilov.moneymanager.goal.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.RemoveGoalNavigator
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import com.d9tilov.moneymanager.goal.vm.dialog.RemoveGoalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalRemoveDialog :
    BaseDialogFragment<RemoveGoalNavigator, FragmentDialogRemoveBinding>(FragmentDialogRemoveBinding::inflate),
    RemoveGoalNavigator {

    private val args by navArgs<GoalRemoveDialogArgs>()
    private val goal by lazy { args.goal }

    override fun getNavigator() = this

    override val viewModel by viewModels<RemoveGoalViewModel>()

    override fun closeDialog() {
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            removeDialogTitle.text = getString(R.string.goals_remove_dialog_title)
            removeDialogSubtitle.text = getString(R.string.goals_remove_dialog_subtitle)
            removeButtonConfirm.setOnClickListener { viewModel.remove(goal) }
            removeButtonCancel.setOnClickListener {
                dismiss()
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    ARG_UNDO_REMOVE_LAYOUT_DISMISS,
                    true
                )
            }
        }
    }

    companion object {
        const val ARG_UNDO_REMOVE_LAYOUT_DISMISS = "arg_undo_remove_layout_dismiss"
    }
}
