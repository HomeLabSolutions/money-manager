package com.d9tilov.android.budget.domain.impl

import com.d9tilov.android.budget.data.contract.BudgetRepo
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.data.model.BudgetData
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import kotlinx.coroutines.flow.firstOrNull

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val currencyInteractor: CurrencyInteractor
) : BudgetInteractor {

    override fun get() = budgetRepo.get()
    override suspend fun create() = budgetRepo.create(currencyInteractor.getMainCurrency().code)

    override suspend fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)
    override suspend fun updateBudgetWithCurrency(currency: String) {
        val budget = budgetRepo.get().firstOrNull()
        budget?.let {
            val newAmount =
                currencyInteractor.toTargetCurrency(it.sum, it.currencyCode, currency)
            val newSavedAmount = currencyInteractor.toTargetCurrency(
                it.saveSum,
                it.currencyCode,
                currency
            )
            budgetRepo.update(
                it.copy(
                    sum = newAmount,
                    saveSum = newSavedAmount,
                    currencyCode = currency
                )
            )
        }
    }

    override suspend fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
