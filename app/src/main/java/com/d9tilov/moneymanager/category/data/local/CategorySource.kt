package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface CategorySource : Source {

    fun createExpenseDefaultCategories(): Completable
    fun create(category: Category): Single<Long>
    fun update(category: Category): Completable
    fun getById(id: Long): Maybe<Category>
    fun getByParentId(id: Long): Maybe<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flowable<List<Category>>
    fun delete(category: Category): Completable
}
