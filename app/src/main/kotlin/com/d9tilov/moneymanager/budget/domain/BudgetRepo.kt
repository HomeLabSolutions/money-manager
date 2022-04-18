package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetRepo {

    suspend fun insert(budgetData: BudgetData)
    fun get(): Flow<BudgetData>
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
