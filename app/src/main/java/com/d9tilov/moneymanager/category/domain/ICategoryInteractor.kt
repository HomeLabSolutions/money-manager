package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.data.entities.Category
import io.reactivex.Completable
import io.reactivex.Flowable

interface ICategoryInteractor {

    fun createExpenseDefaultCategories(): Completable
    fun getExpenseCategories(): Flowable<List<Category>>
    fun getAllCategories(): Flowable<List<Category>>
    fun getChildrenByParent(parentCategory: Category): Flowable<List<Category>>
    fun updateCategory(category: Category): Completable
    fun deleteCategory(category: Category): Completable
}
