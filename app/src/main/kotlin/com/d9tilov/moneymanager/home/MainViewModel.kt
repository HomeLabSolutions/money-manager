package com.d9tilov.moneymanager.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.category.domain.contract.CategoryInteractor
import com.d9tilov.android.common.android.di.CoroutinesModule.Companion.DISPATCHER_IO
import com.d9tilov.android.core.constants.DataConstants.TAG
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.TransactionType
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
        private val transactionInteractor: TransactionInteractor,
        private val billingInteractor: BillingInteractor,
        private val preferencesStore: PreferencesStore,
        private val backupInteractor: BackupInteractor,
        private val userInteractor: UserInteractor,
        private val categoryInteractor: Lazy<CategoryInteractor>,
    ) : ViewModel() {
        private val auth = FirebaseAuth.getInstance()

        private val updateCurrencyExceptionHandler =
            CoroutineExceptionHandler { _, exception ->
                Timber.tag(TAG).d("Unable to update currency: $exception")
            }

        private val uiState: MutableStateFlow<MainActivityUiState> =
            MutableStateFlow(MainActivityUiState.Loading)
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
                                    try {
                                        backupInteractor.restoreBackup()
                                    } catch (ex: NetworkException) {
                                        Timber.tag(TAG).d("Do work with network exception: $ex")
                                    } catch (ex: WrongUidException) {
                                        Timber.tag(TAG).d("Do work with wrong uid exception: $ex")
                                    } catch (ex: FileNotFoundException) {
                                        Timber.tag(TAG).d("Do work with file not found error: $ex")
                                    } catch (ex: FirebaseException) {
                                        Timber.tag(TAG).d("Do work with exception: $ex")
                                    }
                                    user = userInteractor.getCurrentUser().firstOrNull()
                                    if (user == null || user.showPrepopulate) {
                                        userInteractor.createUser(auth.currentUser.toDataModel())
                                        categoryInteractor.get().createDefaultCategories()
                                        MainActivityUiState.Success.Prepopulate
                                    } else {
                                        MainActivityUiState.Success.Main
                                    }
                                } else {
                                    if (userInteractor.showPrepopulate()) {
                                        MainActivityUiState.Success.Prepopulate
                                    } else {
                                        MainActivityUiState.Success.Main
                                    }
                                }
                            }
                        }
                    }.collectLatest { state -> uiState.value = state }
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
                    Timber.tag(TAG).d("Update data. FirebaseUser: $firebaseUser")
                    preferencesStore.updateUid(firebaseUser.uid) // need for dataBase decryption
                    try {
                        backupInteractor.restoreBackup()
                    } catch (ex: NetworkException) {
                        Timber.tag(TAG).d("Do work with network exception: $ex")
                    } catch (ex: WrongUidException) {
                        Timber.tag(TAG).d("Do work with wrong uid exception: $ex")
                    } catch (ex: FileNotFoundException) {
                        Timber.tag(TAG).d("Do work with file not found error: $ex")
                    } catch (ex: FirebaseException) {
                        Timber.tag(TAG).d("Do work with exception: $ex")
                    }
                }
            }
        }

        override fun onCleared() {
            billingInteractor.terminateBillingConnection()
        }

        fun setToLoadingState() {
            uiState.value = MainActivityUiState.Loading
        }
    }

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    sealed interface Success : MainActivityUiState {
        data object Auth : Success

        data object Prepopulate : Success

        data object Main : Success
    }
}
