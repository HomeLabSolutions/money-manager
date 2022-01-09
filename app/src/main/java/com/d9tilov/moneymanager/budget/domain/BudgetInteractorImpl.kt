package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.first
import java.math.BigDecimal

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val userInteractor: UserInteractor,
) : BudgetInteractor {

    override suspend fun create(budgetData: BudgetData) {
        val user = userInteractor.getCurrentUser().first()
        budgetRepo.insert(budgetData.copy(currencyCode = user.currentCurrencyCode))
    }

    override fun get() = budgetRepo.get()
    override suspend fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)
    override suspend fun updateCurrency(
        sum: BigDecimal,
        savedSum: BigDecimal,
        currencyCode: String
    ) {
        val budget = budgetRepo.get().first()
        budgetRepo.update(budget.copy(sum = sum, saveSum = savedSum, currencyCode = currencyCode))
    }

    override suspend fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
