package com.d9tilov.moneymanager.budget.domain

import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import io.reactivex.Completable
import io.reactivex.Single

interface BudgetInteractor {

    fun create(budgetData: BudgetData): Completable
    fun get(): Single<BudgetData>
    fun getCount(): Single<Int>
    fun update(budgetData: BudgetData): Completable
    fun delete(budgetData: BudgetData): Completable
}
