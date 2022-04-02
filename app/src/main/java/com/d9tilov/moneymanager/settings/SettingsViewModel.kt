package com.d9tilov.moneymanager.settings

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.Status
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
) : BaseViewModel<SettingsNavigator>() {

    val userData = userInteractor.getCurrentUser().distinctUntilChanged().asLiveData()

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            postLoading(true)
            val result = userInteractor.backup()
            if (result.status == Status.SUCCESS) {
                val backupData = result.data
                backupData?.let {
                    val user = userInteractor.getCurrentUser().first()
                    userInteractor.updateUser(user.copy(backupData = it))
                    postMessage(R.string.settings_backup_succeeded)
                } ?: postMessage(R.string.settings_backup_error)
            } else {
                postMessage(R.string.settings_backup_error)
            }
            postLoading(false)
        }
    }

    fun changeFiscalDay(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userInteractor.getCurrentUser().first()
            userInteractor.updateUser(user.copy(fiscalDay = day))
        }
        navigator?.save()
    }
}
