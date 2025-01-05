package com.d9tilov.android.currency.observer.impl

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver

class CurrencyUpdateObserverImpl(
    private val currencyInteractor: CurrencyInteractor,
    private val budgetInteractor: BudgetInteractor,
) : CurrencyUpdateObserver {
    override suspend fun updateMainCurrency(code: String) {
        currencyInteractor.updateMainCurrency(code)
        budgetInteractor.updateBudgetWithCurrency(code)
    }
}
