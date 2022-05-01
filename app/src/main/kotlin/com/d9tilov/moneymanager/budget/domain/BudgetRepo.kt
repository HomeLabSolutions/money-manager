package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetRepo {

    fun get(): Flow<BudgetData>
    suspend fun create()
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
