package com.d9tilov.moneymanager.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.ResultOf
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.GeocodingInteractor
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.exception.NetworkException
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.user.data.impl.mapper.toDataModel
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.FileNotFoundException
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        private val analyticsSender: AnalyticsSender,
        private val transactionInteractor: TransactionInteractor,
        private val billingInteractor: BillingInteractor,
        private val preferencesStore: PreferencesStore,
        private val backupInteractor: BackupInteractor,
        private val userInteractor: UserInteractor,
        private val categoryInteractor: Lazy<CategoryInteractor>,
        private val currencyInteractor: CurrencyInteractor,
        private val geocodingInteractor: GeocodingInteractor,
    ) : ViewModel() {
        private val auth = FirebaseAuth.getInstance()

        private val updateCurrencyExceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                Timber.tag(TAG).d("Unable to update currency: $exception")
            }

        private val uiState: MutableStateFlow<MainActivityUiState> =
            MutableStateFlow(MainActivityUiState.Loading)
        private val localCurrencyState = MutableStateFlow(LocationCurrencyState())
        val uiStateFlow: StateFlow<MainActivityUiState> = uiState

        init {
            viewModelScope.launch {
                preferencesStore.uid
                    .map { uid ->
                        val firebaseUid = auth.currentUser?.uid
                        if (firebaseUid == null) {
                            MainActivityUiState.Success.Auth
                        } else {
                            if (firebaseUid != uid) {
                                if (uid != null) userInteractor.deleteUser()
                                auth.signOut()
                                MainActivityUiState.Success.Auth
                            } else {
                                var user = userInteractor.getCurrentUser().firstOrNull()
                                if (user == null) {
                                    when (val result = backupInteractor.restoreBackup()) {
                                        is ResultOf.Success -> {
                                            Timber.tag(TAG).d("Database restored successfully")
                                        }
                                        is ResultOf.Failure -> {
                                            when (result.throwable) {
                                                is NetworkException ->
                                                    Timber
                                                        .tag(TAG)
                                                        .d("Do work with network exception: ${result.throwable}")

                                                is WrongUidException ->
                                                    Timber
                                                        .tag(TAG)
                                                        .d("Do work with wrong uid exception: ${result.throwable}")

                                                is FileNotFoundException ->
                                                    Timber
                                                        .tag(TAG)
                                                        .d("Do work with file not found error: ${result.throwable}")

                                                is FirebaseException ->
                                                    Timber
                                                        .tag(TAG)
                                                        .d("Do work with Firebase exception: ${result.throwable}")

                                                else -> Timber.tag(TAG).d("Do work with exception: ${result.throwable}")
                                            }
                                        }

                                        else -> {}
                                    }
                                    user = userInteractor.getCurrentUser().firstOrNull()
                                    if (user == null || user.showPrepopulate) {
                                        userInteractor.createUser(auth.currentUser.toDataModel())
                                        categoryInteractor.get().createDefaultCategories()
                                        MainActivityUiState.Success.Prepopulate
                                    } else {
                                        MainActivityUiState.Success.Main(localCurrencyState.value)
                                    }
                                } else {
                                    if (userInteractor.showPrepopulate()) {
                                        MainActivityUiState.Success.Prepopulate
                                    } else {
                                        MainActivityUiState.Success.Main(localCurrencyState.value)
                                    }
                                }
                            }
                        }
                    }.collectLatest { state -> uiState.update { state } }
            }
        }

        private val premiumFlow =
            billingInteractor
                .isPremium()
                .flowOn(ioDispatcher)
                .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

        init {
            billingInteractor.startBillingConnection()
            viewModelScope.launch(ioDispatcher + updateCurrencyExceptionHandler) {
                billingInteractor.billingConnectionReady
                    .combine(premiumFlow) { isReady, isPremium ->
                        isReady && isPremium
                    }.collect { readyForPremium ->
                        if (readyForPremium) {
                            launch { transactionInteractor.executeRegularIfNeeded(TransactionType.INCOME) }
                            launch { transactionInteractor.executeRegularIfNeeded(TransactionType.EXPENSE) }
                        }
                    }
            }
        }

        fun updateData() {
            Timber.tag(TAG).d("Update data")
            viewModelScope.launch(ioDispatcher) {
                auth.currentUser?.let { firebaseUser ->
                    analyticsSender.sendWithParams(AnalyticsEvent.Client.Auth.Login) {
                        AnalyticsParams.LoginResultProvider.name to firebaseUser.providerData.firstOrNull()?.providerId
                    }
                    Timber.tag(TAG).d("Update data. FirebaseUser: $firebaseUser")
                    preferencesStore.updateUid(firebaseUser.uid) // need for dataBase decryption
                    when (val result = backupInteractor.restoreBackup()) {
                        is ResultOf.Success -> {
                            Timber.tag(TAG).d("Database restored successfully")
                        }
                        is ResultOf.Failure -> {
                            analyticsSender.sendWithParams(AnalyticsEvent.Internal.Error.NetworkException) {
                                AnalyticsParams.Exception to result.toString()
                            }
                            when (result.throwable) {
                                is NetworkException ->
                                    Timber
                                        .tag(TAG)
                                        .d("Do work with network exception: ${result.throwable}")

                                is WrongUidException ->
                                    Timber
                                        .tag(TAG)
                                        .d("Do work with wrong uid exception: ${result.throwable}")

                                is FileNotFoundException ->
                                    Timber
                                        .tag(TAG)
                                        .d("Do work with file not found error: ${result.throwable}")

                                is FirebaseException ->
                                    Timber
                                        .tag(TAG)
                                        .d("Do work with Firebase exception: ${result.throwable}")

                                else -> Timber.tag(TAG).d("Do work with exception: ${result.throwable}")
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        override fun onCleared() {
            billingInteractor.terminateBillingConnection()
        }

        fun setToLoadingState() {
            uiState.update { MainActivityUiState.Loading }
        }

        fun getLocationCurrencyCode(location: Location) =
            viewModelScope.launch(ioDispatcher + updateCurrencyExceptionHandler) {
                if (uiState.value !is MainActivityUiState.Success.Main) return@launch
                val locationCurrency = geocodingInteractor.getCurrencyByCoords(location.latitude, location.longitude)
                val newState =
                    when (locationCurrency.code) {
                        preferencesStore.getLocalCurrency().firstOrNull() -> {
                            LocationCurrencyState(false, locationCurrency.code)
                        }

                        currencyInteractor.getMainCurrency().code -> {
                            geocodingInteractor.resetLocalCurrency()
                            LocationCurrencyState(false, currencyInteractor.getMainCurrency().code)
                        }

                        else -> {
                            LocationCurrencyState(true, locationCurrency.code)
                        }
                    }
                localCurrencyState.update { newState }
                uiState.update { MainActivityUiState.Success.Main(newState) }
            }

        fun updateCurrency(currencyCode: String?) =
            viewModelScope.launch {
                if (currencyCode == null) return@launch
                if (uiState.value !is MainActivityUiState.Success.Main) return@launch
                launch { currencyInteractor.updateMainCurrency(currencyCode) }
                launch { geocodingInteractor.resetLocalCurrency() }
                val newState = LocationCurrencyState(false, currencyCode)
                localCurrencyState.update { newState }
                uiState.update { MainActivityUiState.Success.Main(newState) }
            }

        fun updateLocalCurrency(currencyCode: String?) =
            viewModelScope.launch {
                if (currencyCode == null) return@launch
                geocodingInteractor.updateLocalCurrency(currencyCode)
                val newState = LocationCurrencyState(false, currencyCode)
                localCurrencyState.update { newState }
                uiState.update { MainActivityUiState.Success.Main(newState) }
            }
    }

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    sealed interface Success : MainActivityUiState {
        data object Auth : Success

        data object Prepopulate : Success

        data class Main(
            val locationCurrencyState: LocationCurrencyState,
        ) : Success
    }
}

data class LocationCurrencyState(
    val showDialog: Boolean = false,
    val currencyCode: String? = null,
)
