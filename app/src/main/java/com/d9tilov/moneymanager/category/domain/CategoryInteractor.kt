package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.category.data.entities.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface CategoryInteractor : Interactor {

    fun create(category: Category): Single<Long>
    fun update(category: Category): Completable
    fun getCategoryById(id: Long): Maybe<Category>
    fun getGroupedCategoriesByType(type: TransactionType): Flowable<List<Category>>
    fun getAllCategoriesByType(type: TransactionType): Flowable<List<Category>>
    fun getChildrenByParent(parentCategory: Category): Flowable<List<Category>>
    fun deleteCategory(category: Category): Completable
    fun deleteSubCategory(subCategory: Category): Single<Boolean>
    fun deleteFromGroup(subCategory: Category): Single<Boolean>
}
