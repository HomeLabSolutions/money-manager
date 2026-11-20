package com.d9tilov.android.widget.domain.impl

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
@Inject
class SpendingWidgetInteractorImpl(
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val observeSpendingSummary: ObserveSpendingSummaryUseCase,
    private val users: UserInteractor,
    private val billingInteractor: BillingInteractor,
) : SpendingWidgetInteractor {
    override fun observe(): Flow<SpendingWidgetState> =
        users
            .getCurrentUser()
            .flatMapLatest { user ->
                when {
                    user == null -> flowOf(SpendingWidgetState.SignedOut)
                    user.showPrepopulate -> flowOf(SpendingWidgetState.SetupRequired)
                    else -> observePremium()
                }
            }.flowOn(ioDispatcher)

    private fun observePremium(): Flow<SpendingWidgetState> =
        billingInteractor.isPremium().distinctUntilChanged().flatMapLatest { premium ->
            if (premium) observeAmounts() else flowOf(SpendingWidgetState.PremiumRequired)
        }

    private fun observeAmounts(): Flow<SpendingWidgetState> =
        observeSpendingSummary().map { summary ->
            SpendingWidgetState.Ready(spendingSummary = summary)
        }
}
