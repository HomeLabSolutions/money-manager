package com.d9tilov.moneymanager.currency.domain

import com.d9tilov.moneymanager.budget.domain.BudgetInteractor

class UpdateCurrencyInteractorImpl(
    private val currencyInteractor: CurrencyInteractor,
    private val budgetInteractor: BudgetInteractor
) : UpdateCurrencyInteractor {

    override suspend fun updateCurrency(code: String) {
        currencyInteractor.updateMainCurrency(code)
        budgetInteractor.updateBudgetWithCurrency(code)
    }
}
