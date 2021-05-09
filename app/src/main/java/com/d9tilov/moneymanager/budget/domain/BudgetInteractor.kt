package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData

interface BudgetInteractor {

    suspend fun create(budgetData: BudgetData)
    suspend fun get(): BudgetData
    suspend fun getCount(): Int
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
