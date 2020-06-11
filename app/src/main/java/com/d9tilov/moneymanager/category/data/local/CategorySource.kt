package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.category.data.entities.Category
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface CategorySource : Source {

    fun createExpenseDefaultCategories(): Completable
    fun create(category: Category): Completable
    fun update(category: Category): Completable
    fun getById(id: Long): Maybe<Category>
    fun getByParentId(id: Long): Maybe<List<Category>>
    fun getAllExpense(): Flowable<List<Category>>
    fun getAllIncome(): Flowable<List<Category>>
    fun delete(category: Category): Completable
}
