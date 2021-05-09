package com.d9tilov.moneymanager.budget.data

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.BudgetSource
import com.d9tilov.moneymanager.budget.domain.BudgetRepo

class BudgetDataRepo(private val budgetSource: BudgetSource) : BudgetRepo {

    override suspend fun insert(budgetData: BudgetData) = budgetSource.insert(budgetData)
    override suspend fun get() = budgetSource.get()
    override suspend fun getCount() = budgetSource.getCount()
    override suspend fun update(budgetData: BudgetData) = budgetSource.update(budgetData)
    override suspend fun delete(budgetData: BudgetData) = budgetSource.delete(budgetData)
}
