package com.d9tilov.android.settings.ui.vm

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.constants.DataConstants.UNKNOWN_BACKUP_DATE
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.utils.toBackupDate
import com.d9tilov.android.network.exception.NetworkException
import com.d9tilov.android.settings_ui.R
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject

data class SettingsUiState(
    val subscriptionState: SubscriptionUiState? = null,
    val startPeriodDay: String = "1",
    val backupState: BackupState = BackupState(),
)

data class BackupState(
    val lastBackupTimestamp: String = "",
    val backupLoading: Boolean = false,
    val showBackupCloseBtn: Boolean = false,
)

data class SubscriptionUiState(
    @StringRes val title: Int = R.string.settings_subscription_premium_title,
    @StringRes val description: Int = R.string.settings_subscription_premium_description,
    val minPrice: SubscriptionPriceUiState? = null,
)

data class SubscriptionPriceUiState(
    val amount: String = "",
    val code: String = "",
    val symbol: String = "",
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupInteractor: BackupInteractor,
    private val userInteractor: UserInteractor,
    billingInteractor: BillingInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()
    private val _errorMessage = MutableSharedFlow<Int>()
    val errorMessage = _errorMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                userInteractor.getCurrentUser(),
                backupInteractor.getBackupData(),
                billingInteractor.getPremiumInfo()
            ) { user, backupData, premiumInfo ->
                val price = if (premiumInfo.isPremium) {
                    SubscriptionPriceUiState(
                        premiumInfo.minBillingPrice.value.toString(),
                        premiumInfo.minBillingPrice.code,
                        premiumInfo.minBillingPrice.symbol
                    )
                } else {
                    null
                }
                val subscriptionState = if (premiumInfo.canPurchase) {
                    SubscriptionUiState(
                        title = if (premiumInfo.isPremium) {
                            R.string.settings_subscription_premium_acknowledged_title
                        } else {
                            R.string.settings_subscription_premium_title
                        },
                        description = when (premiumInfo.isPremium) {
                            true -> if (premiumInfo.hasActiveSku) {
                                R.string.settings_subscription_premium_acknowledged_subtitle_renewing
                            } else {
                                R.string.settings_subscription_premium_acknowledged_subtitle_cancel
                            }

                            false -> R.string.settings_subscription_premium_description
                        },
                        minPrice = price
                    )
                } else {
                    null
                }
                val fiscalDay = user?.fiscalDay ?: 1
                val curValue = _uiState.value
                curValue.copy(
                    subscriptionState = subscriptionState,
                    startPeriodDay = fiscalDay.toString(),
                    backupState = curValue.backupState.copy(
                        lastBackupTimestamp = backupData.lastBackupTimestamp.toBackupDate(),
                        showBackupCloseBtn = backupData.lastBackupTimestamp != UNKNOWN_BACKUP_DATE
                    )
                )
            }.collect { state ->
                _uiState.update { state }
                Timber.tag(TAG).d("Backup completed3: ${_uiState.value}")
            }
        }
    }

    fun backup() {
        viewModelScope.launch {
            _uiState.update { state -> state.copy(backupState = state.backupState.copy(backupLoading = true)) }
            try {
                backupInteractor.makeBackup()
                _errorMessage.emit(R.string.settings_backup_succeeded)
            } catch (ex: NetworkException) {
                Timber.tag(TAG).d("Do work with network exception: $ex")
                _errorMessage.emit(R.string.settings_backup_network_error)
            } catch (ex: WrongUidException) {
                Timber.tag(TAG).d("Do work with wrong uid exception: $ex")
                _errorMessage.emit(R.string.settings_backup_user_error)
            } catch (ex: FileNotFoundException) {
                Timber.tag(TAG).d("Do work with file not found error: $ex")
                _errorMessage.emit(R.string.settings_backup_file_not_found_error)
            } catch (ex: FirebaseException) {
                Timber.tag(TAG).d("Do work with exception: $ex")
                _errorMessage.emit(R.string.settings_backup_error)
            }
            Timber.tag(TAG).d("Backup completed1: ${_uiState.value}")
            _uiState.update { state -> state.copy(backupState = state.backupState.copy(backupLoading = false)) }
            Timber.tag(TAG).d("Backup completed2: ${_uiState.value}")
        }
    }

    fun deleteBackup() {
        viewModelScope.launch {
            when (val result = backupInteractor.deleteBackup()) {
                is ResultOf.Success -> _errorMessage.emit(R.string.settings_backup_deleted)
                is ResultOf.Failure -> {
                    when (result.throwable) {
                        is NetworkException -> _errorMessage.emit(R.string.settings_backup_network_error)
                        is WrongUidException -> _errorMessage.emit(R.string.settings_backup_user_error)
                        is FileNotFoundException -> _errorMessage.emit(R.string.settings_backup_file_not_found_error)
                        is FirebaseException -> _errorMessage.emit(R.string.settings_backup_error)
                        else -> _errorMessage.emit(R.string.settings_backup_user_error)
                    }
                }
                else -> {}
            }
        }
    }

    fun changeFiscalDay(day: String) {
        _uiState.update { it.copy(startPeriodDay = day) }
    }

    fun save() {
        viewModelScope.launch {
            userInteractor.updateFiscalDay(_uiState.value.startPeriodDay.toInt())
        }
    }
}
