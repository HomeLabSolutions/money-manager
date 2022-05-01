package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import kotlinx.coroutines.flow.first

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val currencyInteractor: CurrencyInteractor
) : BudgetInteractor {

    override fun get() = budgetRepo.get()
    override suspend fun create() = budgetRepo.create()
    override suspend fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)
    override suspend fun updateBudgetWithCurrency(currencyCode: String) {
        val budget = budgetRepo.get().first()
        val newAmount = currencyInteractor.toMainCurrency(budget.sum, budget.currencyCode)
        val newSavedAmount = currencyInteractor.toMainCurrency(budget.saveSum, budget.currencyCode)
        budgetRepo.update(
            budget.copy(
                sum = newAmount,
                saveSum = newSavedAmount,
                currencyCode = currencyCode
            )
        )
    }

    override suspend fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
