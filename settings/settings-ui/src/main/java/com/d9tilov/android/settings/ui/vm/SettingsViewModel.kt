package com.d9tilov.android.settings.ui.vm

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common.android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.network.exception.NetworkException
import com.d9tilov.android.settings.ui.navigation.SettingsNavigator
import com.d9tilov.android.settings_ui.R
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject

private const val UNKNOWN_BACKUP_DATE = -1L

data class SettingsUiState(
    val subscriptionState: SubscriptionUiState? = null,
    val startPeriodDay: String = "1",
    val lastBackupTimestamp: Long = UNKNOWN_BACKUP_DATE,
    val backupLoading: Boolean = false,
    val showBackupCloseBtn: Boolean = false
)

data class SubscriptionUiState(
    @StringRes val title: Int = R.string.settings_subscription_premium_title,
    @StringRes val description: Int = R.string.settings_subscription_premium_description,
    val minPrice: SubscriptionPriceUiState? = null
)

data class SubscriptionPriceUiState(
    val amount: String = "",
    val code: String = "",
    val symbol: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupInteractor: BackupInteractor,
    private val userInteractor: UserInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<SettingsNavigator>() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
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
                SettingsUiState(
                    subscriptionState,
                    fiscalDay.toString(),
                    backupData.lastBackupTimestamp,
                    false,
                    backupData.lastBackupTimestamp != UNKNOWN_BACKUP_DATE
                )
            }.collect { state -> _uiState.update { state } }
        }
    }

    fun backup() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(backupLoading = true) }
            try {
                backupInteractor.makeBackup()
                setMessage(R.string.settings_backup_succeeded)
            } catch (ex: NetworkException) {
                Timber.tag(TAG).d("Do work with network exception: $ex")
                setMessage(R.string.settings_backup_network_error)
            } catch (ex: WrongUidException) {
                Timber.tag(TAG).d("Do work with wrong uid exception: $ex")
                setMessage(R.string.settings_backup_user_error)
            } catch (ex: FileNotFoundException) {
                Timber.tag(TAG).d("Do work with file not found error: $ex")
                setMessage(R.string.settings_backup_file_not_found_error)
            } catch (ex: FirebaseException) {
                Timber.tag(TAG).d("Do work with exception: $ex")
                setMessage(R.string.settings_backup_error)
            }
            _uiState.update { it.copy(backupLoading = false) }
        }
    }

    fun changeFiscalDay(day: String) {
        _uiState.update { it.copy(startPeriodDay = day) }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            userInteractor.updateFiscalDay(_uiState.value.startPeriodDay.toInt())
        }
    }
}
