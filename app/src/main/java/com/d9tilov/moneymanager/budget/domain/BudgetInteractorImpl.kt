package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.user.domain.UserInteractor

class BudgetInteractorImpl(
    private val budgetRepo: BudgetRepo,
    private val userInteractor: UserInteractor
) : BudgetInteractor {

    override fun create(budgetData: BudgetData) = userInteractor.getCurrency()
        .flatMapCompletable { budgetRepo.insert(budgetData.copy(currencyCode = it)) }

    override fun get() = budgetRepo.get()
    override fun getCount() = budgetRepo.getCount()

    override fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)

    override fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
