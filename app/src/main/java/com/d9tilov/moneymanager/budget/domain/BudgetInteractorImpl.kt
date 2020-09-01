package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData

class BudgetInteractorImpl(private val budgetRepo: BudgetRepo) : BudgetInteractor {

    override fun create(budgetData: BudgetData) = budgetRepo.insert(budgetData)
    override fun get() = budgetRepo.get()
    override fun getCount() = budgetRepo.getCount()

    override fun update(budgetData: BudgetData) = budgetRepo.update(budgetData)

    override fun delete(budgetData: BudgetData) = budgetRepo.delete(budgetData)
}
