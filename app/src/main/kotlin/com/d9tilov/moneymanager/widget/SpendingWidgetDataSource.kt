package com.d9tilov.moneymanager.widget

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import com.d9tilov.android.user.domain.contract.UserInteractor
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SpendingWidgetDataSource @Inject constructor(
    private val transactions: TransactionInteractor,
    private val currency: CurrencyInteractor,
    private val users: UserInteractor,
    private val billing: BillingInteractor,
) {
    suspend fun load(uid: String?): SpendingWidgetState {
        if (uid == null) return SpendingWidgetState.SignedOut
        val user = users.getCurrentUser().first()
        if (user == null || user.uid != uid || user.showPrepopulate) return SpendingWidgetState.SetupRequired
        if (!billing.isPremium().first()) return SpendingWidgetState.PremiumRequired
        val currencyCode = currency.getMainCurrency().code
        // Recreate the flows for every refresh so their date bounds follow the current day and fiscal period.
        return combine(
            transactions.ableToSpendToday(),
            transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
            transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE),
        ) { available, today, period ->
            SpendingWidgetState.Ready(
                currencyCode = currencyCode,
                availableToday =
                    when (available) {
                        is TransactionSpendingTodayModel.NORMAL -> available.trSum
                        is TransactionSpendingTodayModel.OVERSPENDING -> available.trSum
                    },
                spentToday = today,
                spentInPeriod = period,
            )
        }.first()
    }
}
