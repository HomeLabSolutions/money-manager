package com.d9tilov.moneymanager.settings

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val backupInteractor: BackupInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<SettingsNavigator>() {

    val lifecycleObserver = billingInteractor.getObserver()

    val userData = userInteractor.getCurrentUser()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile.EMPTY)

    val backupData = backupInteractor.getBackupData()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BackupData.EMPTY)

    val minBillingPrice = billingInteractor.getMinPrice()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, DomainCurrency.EMPTY)

    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    val getActiveSku = billingInteractor.getActiveSku()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    val canPurchase = billingInteractor.canPurchase()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            when (backupInteractor.makeBackup()) {
                is ResultOf.Success -> setMessage(R.string.settings_backup_succeeded)
                else -> setMessage(R.string.settings_backup_error)
            }
            setLoading(false)
        }
    }

    fun changeFiscalDay(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userInteractor.getCurrentUser().firstOrNull()
            user?.let { userInteractor.updateUser(it.copy(fiscalDay = day)) }
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
