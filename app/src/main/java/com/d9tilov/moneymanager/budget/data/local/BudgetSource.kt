package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.budget.data.entity.BudgetData

interface BudgetSource : Source {

    suspend fun insert(budgetData: BudgetData)
    suspend fun get(): BudgetData
    suspend fun getCount(): Int
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
