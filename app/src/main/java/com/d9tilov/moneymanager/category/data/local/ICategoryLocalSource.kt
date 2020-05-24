package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.category.data.entities.Category
import io.reactivex.Completable
import io.reactivex.Flowable

interface ICategoryLocalSource : Source {

    fun createExpenseDefaultCategories(): Completable
    fun create(category: Category): Completable
    fun getById(id: Long): Flowable<Category>
    fun getByParentId(id: Long): Flowable<List<Category>>
    fun getAllExpense(): Flowable<List<Category>>
    fun getAllIncome(): Flowable<List<Category>>
    fun update(category: Category): Completable
    fun delete(category: Category): Completable
}
