package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.first

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val userInteractor: UserInteractor
) : BudgetInteractor {

    override suspend fun create(budgetData: BudgetData) {
        val user = userInteractor.getCurrentUser().first()
        budgetRepo.insert(budgetData.copy(currencyCode = user.mainCurrencyCode))
    }

    override fun get() = budgetRepo.get()
    override suspend fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)
    override suspend fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
