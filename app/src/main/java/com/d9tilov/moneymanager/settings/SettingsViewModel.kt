package com.d9tilov.moneymanager.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Status
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
) : BaseViewModel<SettingsNavigator>() {

    val backupData: LiveData<BackupData> = userInteractor.getBackupData().asLiveData()

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInteractor.backup()
            withContext(Dispatchers.Main) {
                if (result.status == Status.SUCCESS) {
                    setMessage(R.string.backup_succeeded)
                } else {
                    setMessage(R.string.backup_error)
                }
            }
        }
    }
}
