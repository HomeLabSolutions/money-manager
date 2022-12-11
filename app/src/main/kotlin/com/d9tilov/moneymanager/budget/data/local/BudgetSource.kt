package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.android.core.model.Source
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import kotlinx.coroutines.flow.Flow

interface BudgetSource : Source {

    fun get(): Flow<BudgetData>
    suspend fun createIfNeeded(currencyCode: String)
    suspend fun update(budgetData: BudgetData)
    suspend fun delete(budgetData: BudgetData)
}
