package com.d9tilov.android.widget.domain.impl

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import com.d9tilov.android.widget.domain.model.SpendingWidgetState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SpendingWidgetInteractorImpl @Inject constructor(
    private val observeSpendingSummary: ObserveSpendingSummaryUseCase,
    private val currency: CurrencyInteractor,
    private val users: UserInteractor,
    private val billingInteractor: BillingInteractor,
) : SpendingWidgetInteractor {
    override fun observe(uid: String?): Flow<SpendingWidgetState> {
        if (uid == null) return flowOf(SpendingWidgetState.SignedOut)
        return users
            .getCurrentUser()
            .flatMapLatest { user ->
                if (user == null || user.uid != uid || user.showPrepopulate) {
                    flowOf(SpendingWidgetState.SetupRequired)
                } else {
                    observePremium()
                }
            }
    }

    private fun observePremium(): Flow<SpendingWidgetState> =
        billingInteractor.isPremium().distinctUntilChanged().flatMapLatest { premium ->
            if (premium) observeCurrency() else flowOf(SpendingWidgetState.PremiumRequired)
        }

    private fun observeCurrency(): Flow<SpendingWidgetState> =
        currency
            .getMainCurrencyFlow()
            .map { it.code }
            .flatMapLatest(::observeAmounts)

    private fun observeAmounts(currencyCode: String): Flow<SpendingWidgetState> =
        observeSpendingSummary().map { summary ->
            SpendingWidgetState.Ready(
                currencyCode = currencyCode,
                spendingSummary = summary,
            )
        }
}
