package com.d9tilov.moneymanager.budget.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import io.reactivex.Completable
import io.reactivex.Single

interface BudgetSource : Source {

    fun insert(budgetData: BudgetData): Completable
    fun get(): Single<BudgetData>
    fun getCount(): Single<Int>
    fun update(budgetData: BudgetData): Completable
    fun delete(budgetData: BudgetData): Completable
}
