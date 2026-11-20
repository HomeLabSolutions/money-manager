package com.d9tilov.android.transaction.domain.impl

import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.transaction.domain.contract.ObserveSpendingSummaryUseCase
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor
import com.d9tilov.android.transaction.domain.model.SpendingSummary
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Inject
class ObserveSpendingSummaryUseCaseImpl(
    private val transactions: TransactionInteractor,
    private val currency: CurrencyInteractor,
) : ObserveSpendingSummaryUseCase {
    override fun invoke(): Flow<SpendingSummary> =
        combine(
            transactions.ableToSpendToday(),
            transactions.getApproxSumTodayCurrentCurrency(TransactionType.EXPENSE),
            transactions.getApproxSumInFiscalPeriodCurrentCurrency(TransactionType.EXPENSE),
            currency.getMainCurrencyFlow(),
        ) { availableToday, spentToday, spentInPeriod, mainCurrency ->
            SpendingSummary(availableToday, spentToday, spentInPeriod, mainCurrency)
        }
}
