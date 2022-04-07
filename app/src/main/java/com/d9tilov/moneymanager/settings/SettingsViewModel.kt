package com.d9tilov.moneymanager.settings

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
) : BaseViewModel<SettingsNavigator>() {

    val userData = userInteractor.getCurrentUser().distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile.EMPTY)

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            when (userInteractor.backup()) {
                is ResultOf.Success -> setMessage(R.string.settings_backup_succeeded)
                else -> setMessage(R.string.settings_backup_error)
            }
            setLoading(false)
        }
    }

    fun changeFiscalDay(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userInteractor.getCurrentUser().first()
            userInteractor.updateUser(user.copy(fiscalDay = day))
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
