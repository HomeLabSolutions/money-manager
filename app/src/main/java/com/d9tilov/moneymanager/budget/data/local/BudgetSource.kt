package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import io.reactivex.Completable

interface BudgetSource {

    fun insert(budgetData: BudgetData): Completable
    fun update(budgetData: BudgetData): Completable
    fun delete(budgetData: BudgetData): Completable
}
