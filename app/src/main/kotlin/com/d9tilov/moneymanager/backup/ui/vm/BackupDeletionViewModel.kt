package com.d9tilov.moneymanager.backup.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.ui.navigator.SettingsBackupDeletionNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class BackupDeletionViewModel @Inject constructor(
    private val backupInteractor: BackupInteractor
) : BaseViewModel<SettingsBackupDeletionNavigator>() {

    fun deleteBackup() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = backupInteractor.deleteBackup()) {
                is ResultOf.Success -> setMessage(R.string.settings_backup_deleted)
                is ResultOf.Failure -> {
                    when (result.throwable) {
                        is NetworkException -> setMessage(R.string.settings_backup_network_error)
                        is WrongUidException -> setMessage(R.string.settings_backup_user_error)
                        is FileNotFoundException -> setMessage(R.string.settings_backup_file_not_found_error)
                        is FirebaseException -> setMessage(R.string.settings_backup_error)
                    }
                }
                else -> {}
            }
            withContext(Dispatchers.Main) { navigator?.dismiss() }
        }
    }
}
