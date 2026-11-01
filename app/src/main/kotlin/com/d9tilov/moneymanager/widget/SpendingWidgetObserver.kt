package com.d9tilov.moneymanager.widget

import android.content.Context
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.user.domain.contract.UserInteractor
import dagger.Lazy
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class SpendingWidgetObserver @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val scope: CoroutineScope,
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val preferences: PreferencesStore,
    private val transactions: Lazy<TransactionInteractor>,
    private val currency: Lazy<CurrencyInteractor>,
    private val users: Lazy<UserInteractor>,
) {
    private var job: Job? = null

    @Synchronized
    fun start() {
        if (job?.isActive == true) return
        job =
            scope.launch(ioDispatcher) {
                preferences.uid
                    .distinctUntilChanged()
                    .flatMapLatest(::changes)
                    .retryWhen { error, _ ->
                        Timber.w(error, "Unable to observe spending widget changes")
                        delay(RETRY_DELAY_MILLIS)
                        true
                    }.collect { SpendingWidgetWork.refresh(context) }
            }
    }

    @Synchronized
    fun stop() {
        job?.cancel()
        job = null
    }

    private fun changes(uid: String?): Flow<Unit> {
        if (uid == null) return flowOf(Unit)
        return users
            .get()
            .getCurrentUser()
            .map { user -> user?.takeIf { it.uid == uid && !it.showPrepopulate }?.fiscalDay }
            .distinctUntilChanged()
            .flatMapLatest { fiscalDay ->
                if (fiscalDay == null) flowOf(Unit) else dataChanges()
            }
    }

    private fun dataChanges(): Flow<Unit> =
        combine(
            transactions.get().ableToSpendToday(),
            transactions.get().getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
            currency.get().getMainCurrencyFlow(),
        ) { _, _, _ -> Unit }

    companion object {
        private const val RETRY_DELAY_MILLIS = 30_000L

        fun get(context: Context): SpendingWidgetObserver =
            EntryPointAccessors.fromApplication<SpendingWidgetObserverEntryPoint>(context).observer()
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SpendingWidgetObserverEntryPoint {
    fun observer(): SpendingWidgetObserver
}
