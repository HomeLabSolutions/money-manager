package com.d9tilov.moneymanager.settings.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.App
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.backup.data.entity.BackupData
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.ui.navigator.SettingsNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val backupInteractor: BackupInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<SettingsNavigator>() {

    private val minPriceExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to get min price")
    }

    val userData = userInteractor.getCurrentUser()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfile.EMPTY)

    val backupData = backupInteractor.getBackupData()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BackupData.EMPTY)
//    val minBillingPrice = billingInteractor.getMinPrice()
//        .flowOn(Dispatchers.IO + minPriceExceptionHandler)
//        .stateIn(viewModelScope, SharingStarted.Eagerly, DomainCurrency.EMPTY)

    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

//    val getActiveSku = billingInteractor.hasRenewablePremium
//        .flowOn(Dispatchers.IO)
//        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
//
//    val canPurchase = billingInteractor.canPurchase()
//        .flowOn(Dispatchers.IO)
//        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            setLoading(true)
            try {
                backupInteractor.makeBackup()
                setMessage(R.string.settings_backup_succeeded)
            } catch (ex: NetworkException) {
                Timber.tag(App.TAG).d("Do work with network exception: $ex")
                setMessage(R.string.settings_backup_network_error)
            } catch (ex: WrongUidException) {
                Timber.tag(App.TAG).d("Do work with wrong uid exception: $ex")
                setMessage(R.string.settings_backup_user_error)
            } catch (ex: FileNotFoundException) {
                Timber.tag(App.TAG).d("Do work with file not found error: $ex")
                setMessage(R.string.settings_backup_file_not_found_error)
            } catch (ex: FirebaseException) {
                Timber.tag(App.TAG).d("Do work with exception: $ex")
                setMessage(R.string.settings_backup_error)
            }
            setLoading(false)
        }
    }

    fun changeFiscalDay(day: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userInteractor.updateFiscalDay(day)
            withContext(Dispatchers.Main) { navigator?.save() }
        }
    }
}
