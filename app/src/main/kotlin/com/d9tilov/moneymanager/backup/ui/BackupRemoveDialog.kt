package com.d9tilov.moneymanager.backup.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.ui.vm.BackupDeletionViewModel
import com.d9tilov.moneymanager.base.ui.BaseDialogFragment
import com.d9tilov.moneymanager.base.ui.navigator.SettingsBackupDeletionNavigator
import com.d9tilov.moneymanager.core.util.gone
import com.d9tilov.moneymanager.databinding.FragmentDialogRemoveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackupRemoveDialog :
    BaseDialogFragment<SettingsBackupDeletionNavigator, FragmentDialogRemoveBinding>(
        FragmentDialogRemoveBinding::inflate
    ),
    SettingsBackupDeletionNavigator {

    override fun getNavigator() = this
    override val viewModel by viewModels<BackupDeletionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.run {
            removeDialogTitle.text = getString(R.string.settings_backup_delete_title)
            removeDialogSubtitle.gone()
            removeButtonConfirm.setOnClickListener { viewModel.deleteBackup() }
            removeButtonCancel.setOnClickListener { dismiss() }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
    }
}
