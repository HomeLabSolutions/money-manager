package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetSource : Source {

    fun get(): Flow<BudgetData>
    suspend fun create()
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
