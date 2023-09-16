package com.d9tilov.android.backup.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.ui.navigator.SettingsBackupDeletionNavigator
import com.d9tilov.android.backup_ui.R
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.network.exception.NetworkException
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

}
