package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface CategoryRepo {

    fun createExpenseDefaultCategories(): Completable
    fun create(category: Category): Single<Long>
    fun update(category: Category): Completable
    fun getCategoryById(id: Long): Maybe<Category>
    fun getCategoriesByType(type: TransactionType): Flowable<List<Category>>
    fun getChildrenByParent(parentCategory: Category): Single<List<Category>>
    fun deleteCategory(category: Category): Completable
}
