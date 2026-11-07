package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.SpendingSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveSpendingSummaryUseCaseImpl @Inject constructor(
    private val transactions: TransactionInteractor,
) : ObserveSpendingSummaryUseCase {
    override fun invoke(): Flow<SpendingSummary> =
        combine(
            transactions.ableToSpendToday(),
            transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
            transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE),
        ) { availableToday, spentToday, spentInPeriod ->
            SpendingSummary(availableToday, spentToday, spentInPeriod)
        }
}
