package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetSource : Source {

    suspend fun insert(budgetData: BudgetData)
    fun get(): Flow<BudgetData>
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
