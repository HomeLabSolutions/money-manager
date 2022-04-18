package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor
) : BudgetInteractor {

    override suspend fun create(budgetData: BudgetData) {
        val user = userInteractor.getCurrentUser().firstOrNull()
        user?.let { budgetRepo.insert(budgetData.copy(currencyCode = it.currentCurrencyCode)) }
    }

    override fun get() = budgetRepo.get()
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
