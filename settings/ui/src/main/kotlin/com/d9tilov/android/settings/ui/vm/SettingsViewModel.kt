package com.d9tilov.android.settings.ui.vm

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common.android.ui.logout.LogoutHandler
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.constants.DataConstants.UNKNOWN_BACKUP_DATE
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.utils.toBackupDate
import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.network.exception.NetworkException
import com.d9tilov.android.settings.ui.R
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.model.InsightLanguage
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Named

data class SettingsUiState(
    val subscriptionState: SubscriptionUiState? = null,
    val startPeriodDay: String = "1",
    val insightLanguage: InsightLanguage = InsightLanguage.SYSTEM,
    val backupState: BackupState = BackupState(),
    val insightLanguageLabels: Map<InsightLanguage, Int> =
        InsightLanguage.supportedLanguages.associateWith { it.labelRes() },
)

private fun InsightLanguage.labelRes(): Int =
    when (this) {
        InsightLanguage.SYSTEM -> R.string.settings_insight_language_system
        InsightLanguage.ENGLISH -> R.string.settings_insight_language_english
        InsightLanguage.RUSSIAN -> R.string.settings_insight_language_russian
        InsightLanguage.SPANISH -> R.string.settings_insight_language_spanish
        InsightLanguage.PORTUGUESE -> R.string.settings_insight_language_portuguese
        InsightLanguage.ARABIC -> R.string.settings_insight_language_arabic
        InsightLanguage.HINDI -> R.string.settings_insight_language_hindi
        InsightLanguage.CHINESE -> R.string.settings_insight_language_chinese
    }

data class BackupState(
    val lastBackupTimestamp: String = "",
    val backupLoading: Boolean = false,
    val showBackupCloseBtn: Boolean = false,
)

data class SubscriptionUiState(
    @field:StringRes val title: Int = R.string.settings_subscription_premium_title,
    @field:StringRes val description: Int = R.string.settings_subscription_premium_description,
    @field:DrawableRes val icon: Int = com.d9tilov.android.designsystem.R.drawable.ic_money_manager_logo,
    val minPrice: SubscriptionPriceUiState? = null,
)

data class SubscriptionPriceUiState(
    val amount: String = "",
    val code: String = "",
    val symbol: String = "",
)

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        private val backupInteractor: BackupInteractor,
        private val userInteractor: UserInteractor,
        private val insightsInteractor: InsightsInteractor,
        private val logoutHandler: LogoutHandler,
        analyticsSender: AnalyticsSender,
        billingInteractor: BillingInteractor,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SettingsUiState())
        val uiState = _uiState.asStateFlow()
        var message: Int? by mutableStateOf(null)

        init {
            analyticsSender.send(
                AnalyticsEvent.Internal.Screen,
                mapOf(AnalyticsParams.Screen.Name to "settings"),
            )
            viewModelScope.launch(ioDispatcher) {
                val insightLanguage = insightsInteractor.language.first()
                _uiState.update { it.copy(insightLanguage = insightLanguage) }
                combine(
                    userInteractor.getCurrentUser(),
                    backupInteractor.getBackupData(),
                    billingInteractor.getPremiumInfo(),
                ) { user, backupData, premiumInfo ->
                    Timber.tag(TAG).d("PremiumInfo: $premiumInfo, BackupData: $backupData")
                    user to backupData
                }.collect { (user, backupData) ->
                    _uiState.update { state ->
                        state.copy(
                            startPeriodDay = (user?.fiscalDay ?: 1).toString(),
                            backupState =
                                state.backupState.copy(
                                    lastBackupTimestamp = backupData.lastBackupTimestamp.toBackupDate(),
                                    showBackupCloseBtn = backupData.lastBackupTimestamp != UNKNOWN_BACKUP_DATE,
                                ),
                        )
                    }
                }
            }
        }

        fun backup() {
            viewModelScope.launch {
                _uiState.update { state -> state.copy(backupState = state.backupState.copy(backupLoading = true)) }
                when (val result = backupInteractor.makeBackup()) {
                    is ResultOf.Success -> {
                        message = R.string.settings_backup_succeeded
                    }

                    is ResultOf.Failure -> {
                        message =
                            when (result.throwable) {
                                is NetworkException -> R.string.settings_backup_network_error
                                is WrongUidException -> R.string.settings_backup_user_error
                                is FileNotFoundException -> R.string.settings_backup_file_not_found_error
                                is FirebaseException -> R.string.settings_backup_error
                                else -> R.string.settings_backup_user_error
                            }
                    }

                    else -> {}
                }
                Timber.tag(TAG).d("Backup completed1: ${_uiState.value}")
                _uiState.update { state -> state.copy(backupState = state.backupState.copy(backupLoading = false)) }
                Timber.tag(TAG).d("Backup completed2: ${_uiState.value}")
            }
        }

        fun deleteBackup() {
            viewModelScope.launch {
                when (val result = backupInteractor.deleteBackup()) {
                    is ResultOf.Success -> {
                        message = R.string.settings_backup_deleted
                    }

                    is ResultOf.Failure -> {
                        message =
                            when (result.throwable) {
                                is NetworkException -> R.string.settings_backup_network_error
                                is WrongUidException -> R.string.settings_backup_user_error
                                is FileNotFoundException -> R.string.settings_backup_file_not_found_error
                                is FirebaseException -> R.string.settings_backup_error
                                else -> R.string.settings_backup_user_error
                            }
                    }

                    else -> {}
                }
            }
        }

        fun deleteAccount() =
            viewModelScope.launch(ioDispatcher) {
                backupInteractor.deleteBackup()
                Firebase.auth.currentUser
                    ?.delete()
                    ?.await()
                logoutHandler.onLogout()
            }

        fun changeFiscalDay(day: String) {
            _uiState.update { it.copy(startPeriodDay = day) }
        }

        fun changeInsightLanguage(language: InsightLanguage) {
            _uiState.update { it.copy(insightLanguage = language) }
        }

        fun save(onSaved: () -> Unit) {
            val settings = _uiState.value
            viewModelScope.launch(ioDispatcher) {
                userInteractor.updateFiscalDay(settings.startPeriodDay.toInt())
                insightsInteractor.setLanguage(settings.insightLanguage)
                withContext(Dispatchers.Main) { onSaved() }
            }
        }
    }
