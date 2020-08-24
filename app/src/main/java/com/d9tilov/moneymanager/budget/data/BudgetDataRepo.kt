package com.d9tilov.moneymanager.budget.data

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.data.local.BudgetSource
import com.d9tilov.moneymanager.budget.domain.BudgetRepo

class BudgetDataRepo(private val budgetSource: BudgetSource) : BudgetRepo {

    override fun insert(budgetData: BudgetData) = budgetSource.insert(budgetData)

    override fun update(budgetData: BudgetData) = budgetSource.update(budgetData)

    override fun delete(budgetData: BudgetData) = budgetSource.delete(budgetData)
}
