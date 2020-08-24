package com.d9tilov.moneymanager.category.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface CategorySource : Source {

    fun createExpenseDefaultCategories(): Completable
    fun createIncomeDefaultCategories(): Completable
    fun create(category: Category): Single<Long>
    fun update(category: Category): Completable
    fun getById(id: Long): Maybe<Category>
    fun getByParentId(id: Long): Flowable<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flowable<List<Category>>
    fun delete(category: Category): Completable

    /**
     * Delete subcategory
     * @return true - if delete parent group also, false - otherwise
     */
    fun deleteSubcategory(subCategory: Category): Single<Boolean>

    /**
     * Delete subcategory from group
     * @return true - if delete parent group also, false - otherwise
     */
    fun deleteFromGroup(subCategory: Category): Single<Boolean>
}
