package com.d9tilov.moneymanager.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.Status
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
) : BaseViewModel<SettingsNavigator>() {

    val userData: LiveData<UserProfile> = userInteractor.getCurrentUser().asLiveData()

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userInteractor.backup()
            withContext(Dispatchers.Main) {
                if (result.status == Status.SUCCESS) {
                    setMessage(R.string.settings_backup_succeeded)
                } else {
                    setMessage(R.string.settings_backup_error)
                }
            }
        }
    }

    fun changeFiscalDay(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userInteractor.getCurrentUser().first()
            if (user.fiscalDay != day) {
                userInteractor.updateUser(user.copy(fiscalDay = day))
            }
        }
    }
}
