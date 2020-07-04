package com.d9tilov.moneymanager.category.domain

import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.category.data.entities.Category
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

interface CategoryRepo {

    fun createExpenseDefaultCategories(): Completable
    fun create(category: Category): Completable
    fun update(category: Category): Completable
    fun getCategoryById(id: Long): Maybe<Category>
    fun getCategoriesByType(type: CategoryType): Flowable<List<Category>>
    fun getChildrenByParent(parentCategory: Category): Maybe<List<Category>>
    fun deleteCategory(category: Category): Completable
}
