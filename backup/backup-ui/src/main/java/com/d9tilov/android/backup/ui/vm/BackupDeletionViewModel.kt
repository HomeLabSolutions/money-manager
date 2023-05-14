package com.d9tilov.android.backup.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.ui.navigator.SettingsBackupDeletionNavigator
import com.d9tilov.android.backup_ui.R
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.network.exception.NetworkException
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.FileNotFoundException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
