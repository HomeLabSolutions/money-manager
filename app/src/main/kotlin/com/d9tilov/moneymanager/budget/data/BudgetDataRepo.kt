package com.d9tilov.moneymanager.budget.data

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.BudgetSource
import com.d9tilov.moneymanager.budget.domain.BudgetRepo

class BudgetDataRepo(private val budgetSource: BudgetSource) : BudgetRepo {

    override fun get() = budgetSource.get()
    override suspend fun create() = budgetSource.createIfNeeded()
    override suspend fun update(budgetData: BudgetData) = budgetSource.update(budgetData)
    override suspend fun delete(budgetData: BudgetData) = budgetSource.delete(budgetData)
}
