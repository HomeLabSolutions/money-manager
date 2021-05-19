package com.d9tilov.moneymanager.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
) : BaseViewModel<SettingsNavigator>() {

    private val profileExceptionHandler = CoroutineExceptionHandler { _, _ ->
        setMessage(R.string.backup_error)
    }
    val backupData: LiveData<BackupData> = userInteractor.getBackupData().asLiveData()

    fun backup() {
        viewModelScope.launch(profileExceptionHandler) { userInteractor.backup() }
        setMessage(R.string.backup_succeeded)
    }
}
