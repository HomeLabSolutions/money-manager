package com.d9tilov.android.budget.data.impl

import com.d9tilov.android.budget.data.contract.BudgetSource
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import com.d9tilov.android.budget.domain.model.BudgetData

class BudgetDataRepo(
    private val budgetSource: BudgetSource,
) : BudgetRepo {
    override fun get() = budgetSource.get()

    override suspend fun create(currencyCode: String) = budgetSource.createIfNeeded(currencyCode)

    override suspend fun update(budgetData: BudgetData) = budgetSource.update(budgetData)

    override suspend fun delete(budgetData: BudgetData) = budgetSource.delete(budgetData)
}
